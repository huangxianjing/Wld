package com.wld.net.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.wld.net.R;
import com.wld.net.utils.Utils;

/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class MainHeadView extends ViewGroup {
    private Matrix mMatrix;
    private Bitmap mTown;
    private Bitmap mCloud;
    private Bitmap mHab;
    private Bitmap mAircraft;

    private int topTotal;
    private int mTotalDragDistance;//可滑动的总距离
    private int mScreenWidth;
    private int mScreenHeight;

    private static final float TOWN_RATIO = 0.25f;//高宽比
    private static final float CLOUD_RATIO = 0.15625f;
    private static final float HAB_RATIO = 0.19f;

    private static final float AIRCRAFT_INITIAL_LEFT = 0.3f;//飞机初始位置在x轴上的比例
    //初始按下时位置
    float y_down;
    private static final float TOWN_FINAL_RATIO = 1.2f;//最终缩放比例
    private int cloud_final_total_x;//云层能向右移动x轴上的距离
    private int hab_final_total_x;//气球能向左移动x轴上的距离
    private int aircraft_final_total_x;//飞机能向左移动x轴上的距离

    private float move_y;//y轴上移动的距离
    private boolean isHead;

    public MainHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMatrix = new Matrix();
        initiateDimens();
        createBitmaps();
    }

    /**
     * 初始化好各种属性 宽高，间距
     */
    private void initiateDimens() {
        topTotal = Utils.dip2px(getContext(), 120);
        mTotalDragDistance = Utils.dip2px(getContext(), 120);
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        cloud_final_total_x = Utils.dip2px(getContext(), 50);
        hab_final_total_x = Utils.dip2px(getContext(), 50);
        aircraft_final_total_x = Utils.dip2px(getContext(), 50);
    }

    /**
     * 初始化好需要的图片
     */
    private void createBitmaps() {
        mAircraft = BitmapFactory.decodeResource(getResources(), R.drawable.discover_aircraft);
        mAircraft = Bitmap.createScaledBitmap(mAircraft, Utils.dip2px(getContext(), 36), Utils.dip2px(getContext(), 10), true);
        mHab = BitmapFactory.decodeResource(getResources(), R.drawable.discover_hab_h);
        mHab = Bitmap.createScaledBitmap(mHab, mScreenWidth, (int) (mScreenWidth * HAB_RATIO), true);
        mCloud = BitmapFactory.decodeResource(getResources(), R.drawable.discover_cloud_h);
        mCloud = Bitmap.createScaledBitmap(mCloud, mScreenWidth, (int) (mScreenWidth * CLOUD_RATIO), true);
        mTown = BitmapFactory.decodeResource(getResources(), R.drawable.discover_city);
        mTown = Bitmap.createScaledBitmap(mTown, mScreenWidth, (int) (mScreenWidth * TOWN_RATIO), true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        if (moveListener != null) {
            moveListener.moveListener(move_y);
        }
        drawHab(canvas);
        drawCloud(canvas);
        drawAircraft(canvas);
        drawTown(canvas);
        canvas.restore();
    }

    private void reset() {
        move_y = 0;
        invalidate();
        if (moveListener != null) {
            moveListener.menuReset();
        }
    }


    public boolean dispatchTouchEventSupper(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isEnabled() || getContext() == null) {
            return dispatchTouchEventSupper(event);
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isHead) {
                    if (move_y == topTotal && moveListener != null) {
                        moveListener.skip();
                    }
                    reset();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                y_down = event.getY();
                isHead = y_down <= topTotal;
                if (isHead) return true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isHead) {
                    float deltaY = event.getY() - y_down;
                    updataPos(deltaY);
                    invalidate();
                }
                break;

        }
        return dispatchTouchEventSupper(event);
    }

    private void updataPos(float delta) {
        move_y = delta < 0 ? 0 : delta > mTotalDragDistance ? mTotalDragDistance : delta;
        Log.i("", "updataPos: move_y  " + move_y);
    }

    private void drawHab(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();
        float deltaX;
        deltaX = move_y / mTotalDragDistance * hab_final_total_x;
        float offsetY = Utils.dip2px(getContext(), 25);
        float offsetX = -deltaX;
        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mHab, matrix, null);
    }

    private void drawCloud(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();
        float deltaX;
        deltaX = move_y / mTotalDragDistance * cloud_final_total_x;
        float offsetY = Utils.dip2px(getContext(), 15);
        float offsetX = deltaX;
        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mCloud, matrix, null);
    }

    private void drawAircraft(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();
        float deltaX;
        deltaX = move_y / mTotalDragDistance * aircraft_final_total_x;
        float offsetY = Utils.dip2px(getContext(), 30);
        float offsetX = mScreenWidth * AIRCRAFT_INITIAL_LEFT - deltaX;
        matrix.postTranslate(offsetX, offsetY);

        canvas.drawBitmap(mAircraft, matrix, null);
    }

    Paint paint;

    private void drawTown(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();
        float townScale;
        townScale = move_y / mTotalDragDistance * (TOWN_FINAL_RATIO - 1.0f) + 1.0f;
        float offsetY = Utils.dip2px(getContext(), 30);
        float offsetX = -(mScreenWidth * townScale - mScreenWidth) / 2.0f;
        matrix.postScale(townScale, townScale);
        matrix.postTranslate(offsetX, offsetY);

        if (paint == null) paint = new Paint();
        paint.setAlpha((int) (80 + townScale * 20));
        canvas.drawBitmap(mTown, matrix, paint);

    }


    private MoveListener moveListener;

    public void setMoveListener(MoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public interface MoveListener {
        void moveListener(float move);

        void skip();

        void menuReset();
    }
}
