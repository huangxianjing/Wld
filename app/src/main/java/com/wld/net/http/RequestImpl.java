package com.wld.net.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import com.wld.net.base.BaseReq;
import com.wld.net.base.BaseRes;
import com.wld.net.utils.FileCopyUtils;
import com.wld.net.utils.JsonParser;
import com.wld.net.utils.MD5Utils;
import com.wld.net.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/24.
 */
public class RequestImpl {
    public static final String TAG = "RequestImpl";
    //增、删、改等操作超时时间
    public static final int CUD_SOCKET_TIMEOUT = 100000;
    //最大重试请求次数
    public static final int MAX_RETRIES = 0;

    private static RequestImpl instance;

    private Context mContext;

    public static RequestImpl getInstance(Context context) {

        if (instance == null) {
            instance = new RequestImpl(context);
        }
        return instance;

    }

    public RequestImpl(Context context) {
        mContext = context;
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * jsonObject get请求
     *
     * @param listener
     * @param bean
     */
    public void jsonObjectRequest(final RequestListener listener, BaseReq bean) {

        Volley_JsonObjectRequest(Method.GET, bean, null, listener);

    }

    private void Volley_JsonObjectRequest(int get, final BaseReq bean,
                                          JSONObject jsonObject, final RequestListener listener) {
        if (!isNetworkAvailable(mContext)) {
            //表示网络不可用
            ToastUtil.showShort(mContext, "请检查网络");
            return;
        }

        Log.i("vy", "get url = " + bean.generateUrl());

        JsonObjectRequest mRequest = new JsonObjectRequest(get, bean.generateUrl(), jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject arg0) {
                Log.i(TAG, "解析信息=" + arg0.toString());
                String data = arg0.toString();
                if (!TextUtils.isEmpty(data)) {
                    try {
                        int status = CodeConstant.RetrunStatus(data);//获取相应的请求响应码

                        Log.i("vy", "json返回状态 " + status);
                        if (0 == status) {

                            BaseRes res = JsonParser.fromJson(data,
                                    bean.getResClass());

                            res.setStatus(status);
                            JSONObject json = new JSONObject(data);
                            String Msg = json.getString("msg");
                            res.setMsg(Msg);
                            listener.successResponse(res);
                        } else {
                            JSONObject jsonErres = new JSONObject(data);
                            String errMsg = jsonErres
                                    .getString("msg");
                            int code = jsonErres.getInt("code");
                            Log.i("vy", "json返回错误信息 " + errMsg + "code=" + code + "data===" + data);
                            listener.handleExceptionResponse(errMsg, bean.getResClass());//给监听类RequestListener赋值
                            listener.handleExceptionResponse(errMsg, code, bean.getResClass());//给监听类RequestListener赋值
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, error.getMessage());
                listener.handleExceptionResponse(RequestErrorHelper
                        .getMessage(error, mContext), bean.getResClass());
                System.out.println("错误信息==" + error.toString());
            }
        });

        mRequest.setRetryPolicy(new DefaultRetryPolicy(CUD_SOCKET_TIMEOUT,
                MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.addRequest(mRequest, this);
    }

    /**
     * volley post方法
     *
     * @param
     * @param listener
     * @param bean
     */
    public void JsonObjectPostRequest(final int method, final RequestListener listener, final BaseReq bean) {
        if (!isNetworkAvailable(mContext)) {
            //表示网络不可用
            ToastUtil.showShort(mContext, "请检查网络");
            return;
        }
        Log.i("vy", "get url = " + bean.generateUrl());
        StringRequest stringRequest = new StringRequest(method, bean.generateUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("vy", "解析信息=" + response);
                        String data = response;
                        if (!TextUtils.isEmpty(data)) {
                            try {
                                int status = CodeConstant.RetrunStatus(data);//获取相应的请求响应码

                                Log.i("vy", "json返回状态 " + status);
                                if (0 == status) {

                                    BaseRes res = JsonParser.fromJson(data,
                                            bean.getResClass());

                                    res.setStatus(status);
                                    listener.successResponse(res);
                                } else {
                                    JSONObject jsonErres = new JSONObject(data);
                                    String errMsg = jsonErres
                                            .getString("msg");
                                    int code = jsonErres.getInt("code");
                                    Log.i("vy", "json返回错误信息 " + errMsg + "code=" + code + "data==" + data);
                                    listener.handleExceptionResponse(errMsg, bean.getResClass());//给监听类RequestListener赋值
                                    listener.handleExceptionResponse(errMsg, code, bean.getResClass());//给监听类RequestListener赋值
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                System.out.println("错误信息==" + error.getMessage());
                listener.handleExceptionResponse(RequestErrorHelper
                        .getMessage(error, mContext), bean.getResClass());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map = bean.getPostParams();
                return map;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(CUD_SOCKET_TIMEOUT,
                MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.addRequest(stringRequest, this);


    }

    /**
     * <p/>
     * Description: 通用的volley multipart POST请求
     * <p/>
     *
     * @param listener
     * @date 2015-3-4
     * @author ewu
     */
    public void multipartRequestData(final RequestListener listener, final BaseReq bean) {
        if (!isNetworkAvailable(mContext)) {
            //表示网络不可用
            ToastUtil.showShort(mContext, "请检查网络");
            return;
        }
        Log.i("WMTest", "Post multipart url = " + bean.generateUrl());
        MultipartRequest req = new MultipartRequest(bean.generateUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vy", "解析信息=" + response);
                if (!TextUtils.isEmpty(response)) {
                    int status = CodeConstant.RetrunStatus(response);
                    try {
                        if (0 == status) {
                            BaseRes res = JsonParser.fromJson(response, bean.getResClass());
                            res.setStatus(status);
                            listener.successResponse(res);
                        } else {
                            JSONObject dataJson = new JSONObject(response);
                            String errMsg = (String) dataJson.get("msg");
                            int code = dataJson.getInt("code");

                            listener.handleExceptionResponse(errMsg, bean.getResClass());
                            listener.handleExceptionResponse(errMsg, code, bean.getResClass());//给监听类RequestListener赋值
                        }
                    } catch (Exception re) {
                        listener.handleExceptionResponse(re.getLocalizedMessage(), bean.getResClass());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, error.getMessage());

                listener.handleExceptionResponse(RequestErrorHelper.getMessage(error, mContext), bean.getResClass());
            }
        }, bean.getFileParts(), bean.getStringParts());

        req.setRetryPolicy(new DefaultRetryPolicy(CUD_SOCKET_TIMEOUT,
                MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.addRequest(req, this);
    }

    public void multipartRequestData2(final RequestListener listener, final BaseReq bean) {
        if (!isNetworkAvailable(mContext)) {
            //表示网络不可用
            ToastUtil.showShort(mContext, "请检查网络");
            return;
        }
        Log.i("WMTest", "Post multipart url = " + bean.generateUrl());
        MultipartRequest2 req = new MultipartRequest2(bean.generateUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vy", "解析信息=" + response);
                if (!TextUtils.isEmpty(response)) {
                    int status = CodeConstant.RetrunStatus(response);
                    try {
                        if (0 == status) {
                            BaseRes res = JsonParser.fromJson(response, bean.getResClass());
                            res.setStatus(status);
                            listener.successResponse(res);
                        } else {
                            JSONObject dataJson = new JSONObject(response);
                            String errMsg = (String) dataJson.get("msg");
                            int code = dataJson.getInt("code");

                            listener.handleExceptionResponse(errMsg, bean.getResClass());
                            listener.handleExceptionResponse(errMsg, code, bean.getResClass());//给监听类RequestListener赋值
                        }
                    } catch (Exception re) {
                        listener.handleExceptionResponse(re.getLocalizedMessage(), bean.getResClass());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, error.getMessage());

                listener.handleExceptionResponse(RequestErrorHelper.getMessage(error, mContext), bean.getResClass());
            }
        }, bean.getStringParts());

        req.setRetryPolicy(new DefaultRetryPolicy(CUD_SOCKET_TIMEOUT,
                MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.addRequest(req, this);
    }

    /**
     * 获取头像
     *
     * @param url
     */
    public void GetPortraitImage(String url) {
        ImageRequest mRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap arg0) {
//                        ListPublic.mPortraitBit = arg0;
                        //TODO
                    }
                }
                , 200, 200, Bitmap.Config.ARGB_8888, null);
        RequestManager.mRequestQueue.add(mRequest);
    }

    /**
     * 缓存 的请求
     * jsonCacheObjectRequest get请求
     */
    public void jsonCacheObjectRequest(final RequestListener listener, BaseReq bean) {
        Volley_JsonCacheObjectRequest(Method.GET, bean, null, listener);
    }

    private void Volley_JsonCacheObjectRequest(int get, final BaseReq bean,
                                               JSONObject jsonObject, final RequestListener listener) {
        if (!isNetworkAvailable(mContext)) {
            //表示网络不可用
            ToastUtil.showShort(mContext, "请检查网络");
            return;
        }
        Log.i(TAG, "url=" + bean.generateUrl());
        tryLoadCacheResponse(bean, listener);
        JsonObjectRequest mRequest = new JsonObjectRequest(get, bean.generateUrl(), jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject arg0) {
                Log.i(TAG, "解析信息=" + arg0.toString());

                String data = arg0.toString();
                if (!TextUtils.isEmpty(data)) {
                    try {
                        int status = CodeConstant.RetrunStatus(data);//获取相应的请求响应码

                        Log.i("json返回状态", "" + status);
                        if (0 == status) {
                            try {
                                File file = new File(mContext.getFilesDir(), "" + MD5Utils.encode(getCacheMd5FileNameUrlPath(bean.generateUrl())));
//                                ALog.i("save file:" + file.toString());
//                                ALog.i("save file url:" + getCacheMd5FileNameUrlPath(bean.generateUrl()));
                                FileCopyUtils.copy(data.getBytes(), file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            BaseRes res = JsonParser.fromJson(data,
                                    bean.getResClass());

                            res.setStatus(status);
                            JSONObject json = new JSONObject(data);
                            String Msg = json
                                    .getString("msg");
                            res.setMsg(Msg);
                            listener.successResponse(res);
                        } else {
                            JSONObject jsonErres = new JSONObject(data);
                            String errMsg = jsonErres
                                    .getString("msg");
                            int code = jsonErres.getInt("code");

                            Log.i("json返回错误信息", "" + errMsg + "code=" + code + "data===" + data);

                            listener.handleExceptionResponse(errMsg, bean.getResClass());//给监听类RequestListener赋值
                            listener.handleExceptionResponse(errMsg, code, bean.getResClass());//给监听类RequestListener赋值
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, error.getMessage());
                listener.handleExceptionResponse(RequestErrorHelper
                        .getMessage(error, mContext), bean.getResClass());
//                System.out.println("错误信息==" + error.toString());
            }
        });


        mRequest.setRetryPolicy(new DefaultRetryPolicy(CUD_SOCKET_TIMEOUT,
                MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.addRequest(mRequest, this);
    }

    /**
     * 尝试从缓存中读取json数据
     */
    private void tryLoadCacheResponse(BaseReq bean, RequestListener listener) {
//        ALog.i("Try to  load cache response first !");
        if (listener != null && bean != null) {
            try {
                //获取缓存文件
                String urlPath = getCacheMd5FileNameUrlPath(bean.generateUrl());
                File cacheFile = new File(mContext.getFilesDir(), "" + MD5Utils.encode(urlPath));
//                ALog.i("load cacheFile:" + cacheFile.toString());
//                ALog.i("load cacheFile url:" + getCacheMd5FileNameUrlPath(bean.generateUrl()));
                StringWriter sw = new StringWriter();
                FileCopyUtils.copy(new FileReader(cacheFile), sw);
                BaseRes res = JsonParser.fromJson(sw.toString(),
                        bean.getResClass());
                res.setStatus(0);
                listener.successResponse(res);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getCacheMd5FileNameUrlPath(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        String paramsSting = url.getQuery();
        String[] params = paramsSting.split("&");
        String pageIndex = "";
        for (String param : params) {
            if (param.contains("pageindex")) {
                pageIndex = param;
                break;
            }
        }
        return url.getProtocol() + "://" + url.getHost() + url.getPath() + "?" + pageIndex;
    }
}
