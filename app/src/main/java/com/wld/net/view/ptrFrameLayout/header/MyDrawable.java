package com.wld.net.view.ptrFrameLayout.header;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Animatable;
import android.view.View;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class MyDrawable extends Drawable implements Animatable {
    private Context mContext;
    private View mParent;
    private Matrix mMatix;


    public MyDrawable(Context context, View parent) {
        this.mContext = context;
        this.mParent = parent;
        if (mMatix == null) {
            mMatix = new Matrix();
        }


    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
