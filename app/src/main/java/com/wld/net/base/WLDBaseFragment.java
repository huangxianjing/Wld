package com.wld.net.base;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public abstract class WLDBaseFragment extends Fragment {
    protected Context mContext;
    protected View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WLDActManager.add(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = initContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!isInvalidateView()) {

            if (rootView == null) {
                rootView = inflater.inflate(getContentViewID(), container, false);
                initView(rootView);
                setListener(rootView);
            }
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
            return rootView;
        }
        rootView = inflater.inflate(getContentViewID(), container, false);
        initView(rootView);
        setListener(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 设置本界面 的Layout的id,如果是定义view，只要重新写该方法，让返回值为0即可
     */
    protected abstract int getContentViewID();

    /**
     * 初始化界面
     *
     * @param view
     */
    protected abstract void initView(View view);

    /**
     * 初始化界面
     */
    protected abstract Context initContext();

    protected void setListener(View view) {
    }

    /**
     * 是否切换Fragment时重新初始化所有View
     */
    protected abstract boolean isInvalidateView();

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
            ((Activity) mContext).finish();
        }
    }

    // -----------------------------进度框部分--------------------------
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
    public void onDestroyView() {
        super.onDestroyView();
        dismissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WLDActManager.remove(this);
    }
}
