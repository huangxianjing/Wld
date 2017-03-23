package com.wld.net.http;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Administrator on 2015/12/24.
 */
public class CodeConstant {
    /**
     * 成功
     */
    public static final String CODE_SUCCESS = "0";

    /**
     * 失败
     */
    public static final String CODE_FAIL = "1";


    /**
     * token失效
     */
    public static final String CODE_TOKEN_FAIL = "2";

    public static int RetrunStatus(String jsonData){
        if(jsonData == null){
            return 1;
        }
        try {
            JSONTokener tokener = new JSONTokener(jsonData);
            JSONObject jsonObject = new JSONObject(tokener);
            String code = null;
            String innercode = null;
            try {
                code=jsonObject.getString("code").toString();
                //innercode = jsonObject
            } catch (Exception e) {
                return 3;

            }
            if(code.equals(CodeConstant.CODE_SUCCESS)){
                return 0;
            }else if(code.equals(CodeConstant.CODE_FAIL)){
                return 1;
            }else if(code.equals(CodeConstant.CODE_TOKEN_FAIL)){
                return 2;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 1;

    }
}
