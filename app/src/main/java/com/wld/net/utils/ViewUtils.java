package com.wld.net.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/11/8.
 * 功能定位：主要用来协助处理界面的帮助类
 */
public class ViewUtils {

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity, boolean isDrak) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.getWindow(), isDrak)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), isDrak)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    public static int StatusBarLightMode(Activity activity) {
        return StatusBarLightMode(activity, true);
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 展示正在加载的进度对话框
     *
     * @param context
     * @param msg
     * @return
     */
//    public static Dialog createLoadingDialog(Context context, String msg) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.my_progressdialog, null);
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
//        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
//        tipTextView.setText(msg);
//        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
//        loadingDialog.setCancelable(true);
//        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));
//        return loadingDialog;
//    }

    /**
     * 自定义Toast的工具类
     *
     * @param activity    类所在的Activity对象
     * @param massage     要显示的信息
     * @param show_length 显示时间的长短, 常用Toast.LENGTH_LONG ,  Toast.LENGTH_SHORT
     */

    /**
     * 功能：自定义Toast的工具类
     *
     * @param context          类所在的Activity对象
     * @param resourse         加载的布局资源(传 0 时会使用默认布局)
     * @param massage          要显示的信息
     * @param show_length_time 显示时间的长短, 常用Toast.LENGTH_LONG ,  Toast.LENGTH_SHORT
     * @param PicRes           前面可以添加的图片资源
     * @param xPianyi_DP       X轴偏移多少个单位（dp）
     * @param yPianyi_DP       Y轴偏移多少个单位（dp）
     */
//    public static void makeToastShow(Context context, @LayoutRes int resourse, String massage, int show_length_time, @DrawableRes int PicRes, int xPianyi_DP, int yPianyi_DP) {
//        //使用布局加载器，将编写的toast_layout布局加载进来
//        if (resourse == 0) {
//            resourse = R.layout.toast_layout;
//        }
//        View view = LayoutInflater.from(context).inflate(resourse, null);
//        //获取ImageView
//        ImageView image = (ImageView) view.findViewById(R.id.toast_iv);
//        //设置图片
//        image.setImageResource(PicRes);
//        //获取TextView
//        TextView title = (TextView) view.findViewById(R.id.toast_tv);
//        //设置显示的内容
//        title.setText(massage);
//        Toast toast = new Toast(context);
//        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
//        int xPianyi_px = ((int) CommonUtil.dip2Dimension(xPianyi_DP, context));
//        int yPianyi_px = ((int) CommonUtil.dip2Dimension(yPianyi_DP, context));
//        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, xPianyi_px, yPianyi_px);
//        //设置显示时间
//        toast.setDuration(show_length_time);
//
//        toast.setView(view);
//        toast.show();
//    }

}
