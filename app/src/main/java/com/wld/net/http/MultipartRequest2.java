/**
 * <p>
 * Copyright: Copyright (c) 2014
 * Company: ZTE
 * Description: 杩欓噷鍐欒繖涓枃浠舵槸骞蹭粈涔堢敤鐨�
 * </p>
 * @Title MultipartRequest.java
 * @Package com.hbcloud.haojihui.request
 * @version 1.0
 * @author wsy
 * @date 2014-11-3
 */
package com.wld.net.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * MultipartRequest
 * @ClassName:MultipartRequest
 * @Description: Request for sending a "mulitpart/form-data" POST with Volley
 * @author: ewu
 * @date: 2014-11-3
 *
 */

public class MultipartRequest2 extends Request<String>
{
    private MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    private HttpEntity entity;
    private Map<String, String> mHeaders;
    private final Response.Listener<String> mListener;
    private Map<File,String> mFilePartsSpecial = new HashMap<File,String>();

    private Map<String,String> mStringParts = new HashMap<String,String>();

    public MultipartRequest2(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String,String> stringParts)
    {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mStringParts.putAll(stringParts);
        entity = buildMultipartEntity();
    }

    public MultipartRequest2(String url, Map<String, String> headers, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String,String> stringParts)
    {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mStringParts.putAll(stringParts);
        this.mHeaders = headers;
        entity = buildMultipartEntitySpecial();
    }

    private HttpEntity buildMultipartEntity()
    {
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);


        for (Map.Entry<String, String> entry : mStringParts.entrySet())
        {
            try {
                builder.addPart(((String) entry.getKey()),
                        new StringBody((String) entry.getValue(), contentType));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return builder.build();
    }

    private HttpEntity buildMultipartEntitySpecial()
    {
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);

        for (Map.Entry<File, String> entry : mFilePartsSpecial.entrySet())
        {
            builder.addPart(((String) entry.getValue()), new FileBody((File) entry.getKey()));
        }

        for (Map.Entry<String, String> entry : mStringParts.entrySet())
        {
            try {
                builder.addPart(((String) entry.getKey()),
                        new StringBody((String) entry.getValue(), contentType));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return builder.build();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType()
    {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            entity.writeTo(bos);
        }
        catch (IOException e)
        {
            e.printStackTrace();
//            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }

        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(json, getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response)
    {
        mListener.onResponse(response);
    }

}
