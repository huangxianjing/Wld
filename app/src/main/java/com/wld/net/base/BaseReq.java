package com.wld.net.base;


import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/24.
 */
public abstract class BaseReq {

    private HashMap<String, String> headers;
    private Map<String, String> postParams;
    public Map<String, String> getStringParts() {
        return stringParts;
    }
    public void setStringParts(Map<String, String> stringParts) {
        this.stringParts = stringParts;

    }
    private Map<String, String> stringParts;
    public LinkedHashMap<String, File> getFileParts() {
        return fileParts;
    }
    public void setFileParts(LinkedHashMap<String, File> fileParts) {
        this.fileParts = fileParts;
    }
    private LinkedHashMap<String, File> fileParts;

    public Map<String, String> getPostParams() {
        return postParams;
    }

    public void setPostParams(Map<String, String> postParams) {
        this.postParams = postParams;
    }

    public abstract String generateUrl();

    public abstract Class<BaseRes> getResClass();

    public abstract BaseRes getResBean();

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }
}
