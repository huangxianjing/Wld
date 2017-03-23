package com.wld.net.utils;

import android.content.Context;

/**
 * Created by Administrator on 2015/12/26.
 */
public class GetEncryptParameter {
    public static String mData;
    public static String time;
    public static String Token;
    Context mContext = null;

    public GetEncryptParameter(String data, Context context) {
        mData = data;
        time = Utils.GetCurTime();
        mContext = context;
        Configuration con=new Configuration(mContext);
        Token = Utils.getTokenKey(con.mKey, mData, time);
    }
}
