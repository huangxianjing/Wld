package com.wld.net.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Process;
import android.util.Log;

import com.wld.net.utils.ToastUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class WLDActManager {
    private static LinkedList<Activity> acys = new LinkedList<Activity>();
    private static LinkedList<Fragment> fragList = new LinkedList<Fragment>();

    public static void add(Activity acy){
        acys.add(acy);
    }

    public static void remove(Activity acy){
        acys.remove(acy);
    }

    public static void add(Fragment frag){
        fragList.add(frag);
    }

    public static void remove(Fragment frag){
        fragList.remove(frag);
    }
    /**
     * 销毁指定的activity
     * Activity没有被销毁时直接销毁
     * @param clazz
     * 			activity.class
     */
    public static void finishActivity(Class<?> clazz){
        if(clazz != null){
            for (Activity a : acys) {
                if(a.getClass().equals(clazz)){
                    if(!a.isFinishing()){
                        a.finish();
                        acys.remove(a);
                    }
                    break;
                }
            }
        }
    }
    /**
     * 销毁指定的activity
     * @param activity
     * 			activity
     */
    public static void finishActivity(Activity activity){
        if(activity != null){
            if(acys.contains(activity)){
                if(!activity.isFinishing()){
                    activity.finish();
                }
                acys.remove(activity);
            }
        }
    }

    /**
     * 销毁所有Activity
     */
    public static void close(){
        Activity acy;
        while(acys.size() != 0){
            acy = acys.poll();
            if(!acy.isFinishing()){
                acy.finish();
            }
        }
    }

    /**
     * 判断Activity是否处于栈顶
     * @param context
     * @param clazzName
     * 			Activity 类名
     * @return
     */
    public static boolean isTopActivity(Context context, String clazzName){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(100);
        String cmpNameTemp = null;
        for (ActivityManager.RunningTaskInfo runningTaskInfo : list) {
            cmpNameTemp = (runningTaskInfo.topActivity).toString();
            boolean bool = cmpNameTemp.contains(clazzName);
            if(bool){
                return bool;
            }
        }
        return false;
    }

    /**
     * 将app切换到后台
     *
     * @param context
     */
    public static void setAppToBackground(Context context) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(
                new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME), 0);
        ActivityInfo ai = homeInfo.activityInfo;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(ai.packageName, ai.name));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {

        } catch (SecurityException e) {
            Log.e("",
                    "Launcher does not have the permission to launch "
                            + intent
                            + ". Make sure to create a MAIN intent-filter for the corresponding activity "
                            + "or use the exported attribute for this activity.",
                    e);
        }
    }


    /**
     * 判断本应用是否在后台运行
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = (activityManager).getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            String processName = appProcess.processName;
            if (processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("ArrowIM", "后台 "+processName);
                    return true;
                }else{
                    Log.i("ArrowIM", "前台 "+appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    private static long time1 = 0;
    /**
     * 退出程序
     *
     * @return void
     */
    public static void exitApp(Context context) {
        long time2 = System.currentTimeMillis();
        if (time2 - time1 > 3000) {
            time1 = time2;
            ToastUtil.showShort(context, "再按一次退出程序");
        } else {
            WLDActManager.close();
            Process.killProcess(Process.myPid());
        }
    }


}
