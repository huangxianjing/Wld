package com.wld.net.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/2/21.
 */

public class WLDSharedPrefUtil {
    private final static String SP_NAME = "";
    private static SharedPreferences sp;

    private static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp;
    }

    /**
     * 保存布尔值
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean saveBoolean(Context context, String key, boolean value) {
        return getSp(context).edit().putBoolean(key, value).commit();
    }

    /**
     * 保存字符串
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean saveString(Context context, String key, String value) {
        return getSp(context).edit().putString(key, value).commit();
    }

    /**
     * 保存long型
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean saveLong(Context context, String key, long value) {
        return getSp(context).edit().putLong(key, value).commit();
    }

    /**
     * 保存int型
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean saveInt(Context context, String key, int value) {
        return getSp(context).edit().putInt(key, value).commit();
    }

    /**
     * 保存float型
     *
     * @param context
     * @param key
     * @param value
     */
    public static boolean saveFloat(Context context, String key, float value) {
        return getSp(context).edit().putFloat(key, value).commit();
    }

    /**
     * 获取字符值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        return getSp(context).getString(key, defValue);
    }

    /**
     * 获取int值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        return getSp(context).getInt(key, defValue);
    }

    /**
     * 获取long值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(Context context, String key, long defValue) {
        return getSp(context).getLong(key, defValue);
    }

    /**
     * 获取float值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(Context context, String key, float defValue) {
        return getSp(context).getFloat(key, defValue);
    }

    /**
     * 获取布尔值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        return getSp(context).getBoolean(key, defValue);
    }


    /**
     * 清除该Key数据
     */
    public static boolean remove(Context context, String key) {
        return getSp(context).edit().remove(key).commit();
    }

    /**
     * 清除所有SharePre保存的数据
     */
    public boolean clearAllData(Context context) {
        return getSp(context).edit().clear().commit();
    }

}
