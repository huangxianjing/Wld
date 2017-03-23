package com.wld.net.utils;

import android.content.Context;

import com.wld.net.R;


/**
 * Created by Administrator on 2015/12/26.
 */
public class Configuration {
    Context mContext;

    public static String mKey;

    public Configuration(Context context) {
        mContext=context;
        mKey=mContext.getString(R.string.str_url_sign_key);
    }
}
