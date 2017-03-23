package com.wld.net.view.ptrFrameLayout.header;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.wld.net.view.ptrFrameLayout.PtrFrameLayout;
import com.wld.net.view.ptrFrameLayout.PtrIndicator;
import com.wld.net.view.ptrFrameLayout.PtrTensionIndicator;
import com.wld.net.view.ptrFrameLayout.PtrUIHandler;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class MyHeaderView extends View implements PtrUIHandler {

    private MyDrawable mDrawable;
    private PtrFrameLayout mPtrFrameLayout;
    private PtrTensionIndicator mPtrTensionIndicator;

    public MyHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public MyHeaderView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mDrawable = new MyDrawable(getContext(), this);
    }

    private void setUp(PtrFrameLayout ptrFrameLayout) {
        mPtrFrameLayout = ptrFrameLayout;
        mPtrTensionIndicator = new PtrTensionIndicator();
        mPtrFrameLayout.setPtrIndicator(mPtrTensionIndicator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int height = mDrawable.getTotalDragDistance() * 5 / 4;
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int pl = getPaddingLeft();
        int pt = getPaddingTop();
        mDrawable.setBounds(pl, pt, pl + right - left, pt + bottom - top);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
//        mDrawable.resetOriginals();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mDrawable.start();
        float percent = mPtrTensionIndicator.getOverDragPercent();
//        mDrawable.offsetTopAndBottom(mPtrTensionIndicator.getCurrentPosY());
//        mDrawable.setPercent(percent);
        invalidate();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        float percent = mPtrTensionIndicator.getOverDragPercent();
        mDrawable.stop();
//        mDrawable.offsetTopAndBottom(mPtrTensionIndicator.getCurrentPosY());
//        mDrawable.setPercent(percent);
        invalidate();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        float percent = mPtrTensionIndicator.getOverDragPercent();
//        mDrawable.offsetTopAndBottom(mPtrTensionIndicator.getCurrentPosY());
//        mDrawable.setPercent(percent);
        invalidate();
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        if (dr == mDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(dr);
        }
    }
}
