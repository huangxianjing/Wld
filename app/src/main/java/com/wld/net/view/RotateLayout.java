package com.wld.net.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.wld.net.R;
import com.wld.net.SecondActivity;
import com.wld.net.utils.Utils;

/**
 * Created by hxj on 2017/3/24 0024.
 * 参考资料http://www.jianshu.com/p/b3f2d9c800bd
 */

public class RotateLayout extends View {
    float x_down;
    float y_down;
    PointF mid = new PointF();//旋转的中心点
    float oldRotation = 0;
    /**
     * 旋转相关的矩阵
     */
    Matrix matrix = new Matrix();
    Matrix matrix1 = new Matrix();
    Matrix savedMatrix = new Matrix();
    /**
     * 移动相关的矩阵
     */
    Matrix moveMatrix = new Matrix();
    Matrix matrix2 = new Matrix();
    Matrix savedMatrix2 = new Matrix();


    int widthScreen;
    int heightScreen;
    int transW;//地图起始点偏移x
    int transH;//偏移y

    Bitmap gintama;
    Bitmap redPacket;
    Bitmap pop_bg;
    Bitmap coordinate;
    private boolean isReset = true;
    private boolean isShowPop = false;
    private float popX;
    private float popY;
    // 触控模式
    private int mode;
    private static final int NONE = 0; // 无模式
    private static final int ROTATE = 1; // 单点旋转模式

    public RotateLayout(Context context) {
        super(context);
    }


    public RotateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        gintama = BitmapFactory.decodeResource(getResources(), R.drawable.discover_map);
        redPacket = BitmapFactory.decodeResource(getResources(), R.drawable.discover_icon_red);
        pop_bg = BitmapFactory.decodeResource(getResources(), R.drawable.discover_pop_bg);
        coordinate = BitmapFactory.decodeResource(getResources(), R.drawable.discover_coordinate);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mode = NONE;
                x_down = event.getX();
                y_down = event.getY();
                oldRotation = getSpaceRotation(event);
                savedMatrix.set(matrix);//按下时底图的矩阵
                savedMatrix2.set(moveMatrix);//按下时红包的矩阵
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x_down - event.getX()) > 5 || Math.abs(y_down - event.getY()) > 5) {//降低灵敏度
                    mode = ROTATE;
                    // 单点旋转
                    matrix1.set(savedMatrix);
                    float deltaRotation = getSpaceRotation(event) - oldRotation;
                    matrix1.postRotate(deltaRotation, mid.x, mid.y);//旋转后的矩阵也是中转给matrix的矩阵
                    matrix.set(matrix1);

                    matrix2.set(savedMatrix2);
                    RectF r = new RectF();
                    savedMatrix2.mapRect(r);
                    getTranslate(r.left, r.top, deltaRotation);
                    matrix2.postTranslate(newX - r.left, newY - r.top);
                    moveMatrix.set(matrix2);

                    popX = newX - 3 * pop_bg.getWidth() / 5;
                    popY = newY - pop_bg.getHeight() - Utils.dip2px(getContext(), 10);

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mode == NONE) {
                    RectF rectF = new RectF(0, 0, redPacket.getWidth(), redPacket.getHeight());
                    moveMatrix.mapRect(rectF);
                    if (rectF.contains(event.getX() + transW, event.getY())) {
                        popX = rectF.left - 3 * pop_bg.getWidth() / 5;
                        popY = rectF.top - pop_bg.getHeight() - Utils.dip2px(getContext(), 10);
                        isShowPop = true;
                    } else {
                        isShowPop = false;
                    }
                    invalidate();
                }
                break;
        }
        return true;
    }

    float newX, newY;

    /**
     * 获取旋转一定角度后视图的坐标
     *
     * @param x             原x
     * @param y             原y
     * @param deltaRotation 角度
     */
    private void getTranslate(float x, float y, float deltaRotation) {
        float k = (float) Math.toRadians(deltaRotation);//弧度值
        newX = (float) ((x - mid.x) * Math.cos(k) - (y - mid.y) * Math.sin(k) + mid.x);
        newY = (float) ((x - mid.x) * Math.sin(k) + (y - mid.y) * Math.cos(k) + mid.y);
    }

    /**
     * 获取手指的旋转角度
     *
     * @param event
     * @return
     */
    private float getSpaceRotation(MotionEvent event) {
        double deltaX = event.getX() - mid.x + transW;
        double deltaY = event.getY() - mid.y;
        double radians = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radians);
    }


    /**
     * viewGroup中没有onDraw方法，只有dispatchDraw，View中调用onDraw后也会调用diapatchDraw
     */
//    protected void onDraw(Canvas canvas) {
//        canvas.save();
//        canvas.translate(-transW, -transH);
//        canvas.drawBitmap(gintama, matrix, null);
//        canvas.restore();
//    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(-transW, 0);
        canvas.drawBitmap(gintama, matrix, null);
        canvas.drawBitmap(coordinate, mid.x - coordinate.getWidth() / 2, mid.y - coordinate.getHeight() / 2, null);
        if (isReset) {
            moveMatrix.postTranslate(mid.x - redPacket.getWidth() / 2 + 200, mid.y - redPacket.getHeight() - 100);
            isReset = false;
        }
//        canvas.drawBitmap(redPacket, mid.x - redPacket.getWidth() / 2, mid.y - redPacket.getHeight(), null);
        canvas.drawBitmap(redPacket, moveMatrix, null);
        if (isShowPop) {
            canvas.drawBitmap(pop_bg, popX, popY, null);
        }
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthScreen = MeasureSpec.getSize(widthMeasureSpec);
        heightScreen = MeasureSpec.getSize(heightMeasureSpec);
        transW = Math.abs(gintama.getWidth() - widthScreen) / 2;
        transH = Math.abs(gintama.getHeight() - heightScreen) / 2;
        mid.x = widthScreen / 2 + transW;
        mid.y = heightScreen / 2 + transH;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


}
