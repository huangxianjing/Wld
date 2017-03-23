package com.wld.net.http;


import com.wld.net.base.BaseRes;

/**
 * Created by Administrator on 2015/12/24.
 */
public interface RequestListener {
    public abstract void successResponse(BaseRes baseRes);

    public abstract void handleExceptionResponse(String errMssg, Class<BaseRes> baseRes);

    public abstract void handleExceptionResponse(String errMssg, int code, Class<BaseRes> baseRes);
}
