package com.wld.net.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.wld.net.http.RequestManager;

/**
 * Created by Administrator on 2017/2/20.
 */

public abstract class WLDBaseActivity extends FragmentActivity {
    protected WLDBaseActivity mContext;
    /**
     * 设置是否进入页面时就隐藏软键盘，默认隐藏
     */
    private boolean isHiddehSoftInput = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewID());

        if (isHiddehSoftInput) {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        mContext = this;
        WLDActManager.add(mContext);
        init(savedInstanceState);
    }

    /**
     * 设置本界面 的Layout的id,如果是定义view，只要重新写该方法，让返回值为0即可
     */
    protected abstract int getContentViewID();

    /**
     * 设置是否进入页面时就隐藏软键盘，默认隐藏
     */
    public void setHiddehSoftInput(boolean isHiddehSoftInput) {
        this.isHiddehSoftInput = isHiddehSoftInput;
    }

    /**
     * 初始化所有View和数据
     */
    private void init(Bundle savedInstanceState) {
        initView(savedInstanceState);
        getIntentInfo();
        setListener();
    }

    /**
     * 初始化所有控件
     */
    protected void initView(Bundle savedInstanceState) {
    }

    /**
     * 获取 意图 intent中的数据，如果不需要获取，则可以不做处理
     */
    protected void getIntentInfo() {
    }

    /**
     * 设置监听事件，如果不用设置，则不用设置
     */
    protected void setListener() {
    }

    /**
     * 封装Intent跳转
     *
     * @param clazz       要跳向的界面的class
     * @param isCloseSelf 是否关闭本界面
     */
    public void skip(Class<?> clazz, boolean isCloseSelf) {
        Intent intent = new Intent(mContext, clazz);
        startActivity(intent);

        if (isCloseSelf) {
            mContext.finish();
        }
    }

    /**
     * 等待框是否显示
     */
    protected boolean isProgressShowing = false;

    protected ProgressDialog progressDialog;

    /**
     * 显示等待框
     */
    public void showProgressDialog() {
        showProgressDialog(mContext, "", "正在加载，请稍等...");
    }

    /**
     * 显示等待框
     *
     * @param title
     * @param message
     */
    public void showProgressDialog(String title, String message) {
        showProgressDialog(mContext, title, message);
    }

    public void showProgressDialog(Context context, String title, String message) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(context, title, message);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            isProgressShowing = true;
        } else {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    /**
     * 取消等待框
     */
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    isProgressShowing = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WLDActManager.finishActivity(this);
        dismissProgressDialog();
        RequestManager.cancelAll(this);
    }

    @Override
    protected void onResume() {
//        StatService.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
//        StatService.onPause(this);
        super.onPause();
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}