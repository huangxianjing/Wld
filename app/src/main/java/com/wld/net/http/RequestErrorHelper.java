package com.wld.net.http;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wld.net.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取请求的错误的信息
 * Created by Administrator on 2015/12/24.
 */
public class RequestErrorHelper {
    public static Context mContext;

    public static String getMessage(Object error, Context context) {
        mContext = context;
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.generic_server_down);//服务器暂时无法访问,请稍后再试

        } else if (isServerProblem(error)) {

            return handleServerError(error, context);

        } else if (isNetworkProblem(error)) {

            return context.getResources().getString(R.string.no_internet);//目前无网络连接,请连接后再试

        }

        return context.getResources().getString(R.string.generic_error);//网络异常

    }

    /**
     * 是否与网络有关
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * 是否与服务器有关
     *
     * @param error
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * 处理服务器错误,显示具体返回的错误码
     *
     * @param err
     * @param context
     * @return
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {

            switch (response.statusCode) {
                case 1009:
                    break;
                case 404:
                case 422:
                case 401:
                case 403:
                    try {
                        HashMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<Map<String, String>>() {
                                }.getType());
                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //无效请求
                    if (error.getMessage() != null) {
                        return error.getMessage();
                    }
                default:
                    return context.getResources().getString(R.string.generic_server_down);
            }
        }

        return context.getResources().getString(R.string.generic_error);
    }
}
