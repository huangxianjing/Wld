package com.wld.net.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/24.
 */
public class BaseRes implements Serializable {
    /**
     * 请求状态
     * 0 正确 1错误 2token失效
     */
    public int status=-1;

    public String getMsg() {
        return msg;
    }

    private String msg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
