package com.wld.net.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 */

public class ThreadUtils {

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    //创建一个只有一个只有线程的线程池
    private static Executor sExecutor = Executors.newSingleThreadExecutor();

    /**
     *    运行在子线程
     * @param runnable
     */
    public static void runOnSubThread(Runnable runnable){
        sExecutor.execute(runnable);
    }

    /**
     *   运行在主线程
     * @param runnable
     */
    public static void runOnMainThread(Runnable runnable){
        sHandler.post(runnable);

    }

}
