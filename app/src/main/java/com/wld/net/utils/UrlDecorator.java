package com.wld.net.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * URL拼接
 * Created by Administrator on 2015/12/25.
 */
public class UrlDecorator {
    private StringBuilder url;

    public StringBuilder getUrl() {
        return url;
    }

    public void setUrl(StringBuilder url) {
        this.url = url;
    }
    public UrlDecorator(){

    }
    /**
     * 将参数拼接在url问号前面
     *
     * @param paramValue
     * @return
     */
    public UrlDecorator append(String paramValue) {

        if (paramValue != null && !"".equals(paramValue)) {
            int i = this.url.indexOf("?");
            if (i > 0) {
                if (this.url.substring(i - 1, i).equals("/")) {

                    this.url.insert(i, paramValue);
                } else {
                    this.url.insert(i, "/" + paramValue);

                }
            } else {
                if (!this.url.toString().endsWith("/")) {
                    this.url.append("/");
                }
                this.url.append(paramValue);
            }

        } else {
            Log.e("UrlDecorator", "错误：URL路径中出现空字符串或者null对象");
        }
        return this;

    }
    /**
     * 将参数拼装在url问号后面
     *
     * @param params
     * @param baseUrl
     * @return
     */
    public String add(HashMap<String,String> params, String baseUrl){
        String url = baseUrl;
        if(params != null){
            if (params.size()!=0) {
                //转为iterator entryset只是遍历了第一次，他把key和value都放到了entry中,这样速度快
                Iterator it = params.entrySet().iterator();
                StringBuffer sb = null;
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    if (sb == null) {
                        sb = new StringBuffer();
                        sb.append("");
                    } else {
                        sb.append("&");
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
                url += sb.toString();

                return url;
            }
            else {
                return url;
            }
        }
        return url;
    }
    // URI不允许有空格等字符，如果参数值有空格，需要此方法转换
    private String encode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {

            // 针对不支持的编码时报错，utf-8应该是支持的
            Log.e("参数转码异常", e.toString());
            throw new RuntimeException(e);
        }
    }

}
