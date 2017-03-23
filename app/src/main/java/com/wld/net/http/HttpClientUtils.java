package com.wld.net.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;


/**
 * httpclient请求封装类
 *
 * @author HQ
 */
public final class HttpClientUtils {

    /**
     * 得到httpclient对象
     *
     * @return
     */
    public static HttpClient getHttpClient() {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                5000);
        return client;
    }


    /**
     * @param url    请求URL
     * @param params post参数
     * @return
     */
    public static String requestPost(String url, List<NameValuePair> params) {
        HttpClient client = getHttpClient();

        HttpPost post = new HttpPost(url);

        try {
            UrlEncodedFormEntity codeEntity = new UrlEncodedFormEntity(params,
                    HTTP.UTF_8);
            post.setEntity(codeEntity);

            HttpResponse response = client.execute(post);

            if (null != response
                    && response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK)

            {
                HttpEntity entity = response.getEntity();

                String jsonStr = EntityUtils.toString(entity, HTTP.UTF_8);
                if (null != jsonStr && jsonStr.length() > 0) {
                    // System.out.println(jsonStr);
                    return jsonStr.trim();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return null;
    }


    /***
     * get请求
     *
     * @param url 请求url
     * @return json字符串
     */
    public static String requestGet(String url) {
        HttpClient client = getHttpClient();

        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);

            if (null != response
                    && response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, HTTP.UTF_8);
                if (null != jsonStr && jsonStr.length() > 0) {
                    return jsonStr.trim();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }

        return null;
    }


}
