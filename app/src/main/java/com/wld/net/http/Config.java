package com.wld.net.http;

/**
 * 接口api
 */

public interface Config {
//    public static String BASE_URL = "https://mapi.iweilingdi.com/index.php/Api/";//线上地址
    public static String BASE_URL = "https://testapi.iweilingdi.com/index.php/Api/";//测试地址??

    //网页链接
//    public static String BASE_URL01 = "http://m.iweilingdi.com/index.php/Home/";//线上地址
    public static String BASE_URL01 = "http://testapi.iweilingdi.com/index.php/Home/";//测试地址
    String GETOPENID = BASE_URL + "Login/GetUserByCode?";
    String LOGIN = BASE_URL + "Login/login?";
}
