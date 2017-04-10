package com.wld.net.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.wld.net.R;
import com.wld.net.SecondActivity;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class TouchImageView extends ImageView {
    PointF mid = new PointF();//旋转的中心点
    float oldRotation = 0;
    Matrix matrix = new Matrix();
    Matrix matrix1 = new Matrix();
    Matrix savedMatrix = new Matrix();

    int widthScreen;
    int heightScreen;
    int transW;
    int transH;

    Bitmap gintama;

    public TouchImageView(SecondActivity activity) {
        super(activity);
        gintama = BitmapFactory.decodeResource(getResources(), R.drawable.discover_map);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        widthScreen = dm.widthPixels;
        heightScreen = dm.heightPixels;
        transW = Math.abs(gintama.getWidth() - widthScreen) / 2;
        transH = Math.abs(gintama.getHeight() - heightScreen) / 2;
        mid.x = widthScreen / 2 + transW;
        mid.y = heightScreen / 2 + transH;
        matrix = new Matrix();
    }

    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(-transW, -transH);
        canvas.drawBitmap(gintama, matrix, null);
        canvas.restore();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                oldRotation = getSpaceRotation(event);
                savedMatrix.set(matrix);
                break;
            case MotionEvent.ACTION_MOVE:
                // 单点旋转
                matrix1.set(savedMatrix);
                float deltaRotation = getSpaceRotation(event) - oldRotation;
                matrix1.postRotate(deltaRotation, mid.x, mid.y);
                matrix.set(matrix1);
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 获取手指的旋转角度
     *
     * @param event
     * @return
     */
    private float getSpaceRotation(MotionEvent event) {
        double deltaX = event.getX(0) - mid.x + transW;
        double deltaY = event.getY(0) - mid.y + transH;
        double radians = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radians);
    }

}
