package com.wld.net.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/12/25.
 */
public class Utils {
    public static Context mContext;
    public Activity mActivity;
    private float scale;
    private static TextView mTextCode;
    private static View mGetCode;
    //TODO
//    static final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");//初始化分享控件

    public Utils(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public Utils(Context mContext) {
        this.mContext = mContext;
    }

    public static boolean isOnMainThread() {
        if (Thread.currentThread().getName().equals("main")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 数据请求Token生成方法
     *
     * @param key
     * @param data
     * @param time
     * @return
     */
    public static String getTokenKey(String key, String data, String time) {
        String ret = "";
        try {
//            Log.i("TAG", data);
            byte[] data1 = Base64.encode(data.getBytes(), Base64.NO_WRAP);

            String data2 = new String(data1, "UTF-8");
//            Log.i("TAG", data2);
            String data3 = key + time + data2;
//            Log.i("TAG", data3);
            byte[] data4 = Base64.encode(data3.getBytes(), Base64.NO_WRAP);
            String data5 = new String(data4, "UTF-8");
//            Log.i("TAG", data5);
            ret = getMd5Value(data5);
//            Log.i("TAG", ret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * MD5加密函数（32位小写）
     *
     * @param sSecret
     * @return
     */
    public static String getMd5Value(String sSecret) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public final static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成一个32位的随机数
     *
     * @return
     */
    public static String genNonceStr() {
        Random random = new Random();
        return Utils.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 时间蹉
     *
     * @return
     */
    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 生成签名串
     *
     * @param params
     * @return
     */
    public static String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        //TODO
//        sb.append(Constants.API_KEY);


        //this.sb.append("sign str\n"+sb.toString()+"\n\n");
        String appSign = Utils.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        Log.e("orion", appSign);

        return appSign;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String GetCurTime() {
        String ret = System.currentTimeMillis() / 1000 + "";
//        System.out.println("时间=="+ret);

        return ret;
    }


    /**
     * 设置ListView高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();


        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /**
     * 获取验证码的倒计时
     */
    public static void Countdown(TextView textCode, View getCode) {

        final int count = 60;
        mTextCode = textCode;
        mGetCode = getCode;
        mTextCode.setText("60s后重新获取");
        mGetCode.setEnabled(false);
        //final String time = mTextCode.getText().toString().replace("","");

        new Thread() {
            @Override
            public void run() {
                int i = count;
                while (i >= 0) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = i;
                    mHandler.sendMessage(message);
                    SystemClock.sleep(1000);
                    i--;

                }
            }
        }.start();

    }


    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int i = (int) msg.obj;
            switch (msg.what) {
                case 1:
                    mTextCode.setText(i + "s后重新获取");
                    if (i <= 0) {
                        mTextCode.setText("获取验证码");
                        mGetCode.setEnabled(true);
                    }
                    break;

            }
        }
    };

    /**
     * 判断手机号码正则
     */
    public static boolean isPhone(String number) {
//        String format = "[0-9]{11}";
        String format = "^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(14[0-9]))\\d{8}$";//手机号码正则

        String format1 = "^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";//固定电话正则

        Pattern pattern = Pattern.compile(format, Pattern.CASE_INSENSITIVE);
        Pattern pattern1 = Pattern.compile(format1, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        Matcher matcher1 = pattern1.matcher(number);
        if (matcher.matches()) {
            return true;
        } else {
            if (matcher1.matches()) {
                return true;
            }
            return false;
        }

    }

    /**
     * 保存图片
     *
     * @param bit
     * @return
     */
    public static File saveShaiDanPicture(Bitmap bit) {
        String ImgPath = "";
        File f2 = null;
        try {
            String SDPATH = Environment.getExternalStorageDirectory().toString();
            String folder = SDPATH + "/WLD/.Cache/";
            String fileName = "pic_" + System.currentTimeMillis() + ".png";
            ImgPath = folder + fileName;
            File file = new File(ImgPath);
            if (file.exists()) {
                return file;
                // file.delete();
            }

            File f1 = new File(folder);
            if (!f1.exists()) {
                f1.mkdirs();
            }

            f2 = new File(ImgPath);
            FileOutputStream fos = new FileOutputStream(f2);
            bit.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            //    bit.recycle();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return f2;
    }


    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        Bitmap img;
        try {
            URL url1 = new URL(url);

            BitmapFactory.Options options = new BitmapFactory.Options();
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            InputStream is = connection.getInputStream();
            img = BitmapFactory.decodeStream(is, null, options);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片右翻转90度
     *
     * @param bitmap
     * @return
     */
    public static Bitmap RotatePicture(Bitmap bitmap) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            try {
                m.setRotate(90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);//90就是我们需要选择的90度
                Bitmap bmp2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                bitmap.recycle();
                bitmap = bmp2;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                //System.out.println("oldfile==="+oldfile);
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                //  System.out.println("oldfile===复制完毕");
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 截取手机号码   中间四位用****代替
     *
     * @param str
     * @return
     */
    public static String CutOut(String str) {
        String mPhone = str.substring(0, str.length() - (str.substring(3)).length()) + "****" + str.substring(7);
        return mPhone;
    }

    /**TODO
     * 友盟分享功能封装（添加分享平台）
     */
//    public static void addSharPlatform(Activity mActivity, String currentUrl) {
//
//        String appWxId = "wxfc1431e8240cba36";
//        String appSecret = "d0b753a1f1e0e3033eabc439f8368b55";
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(mActivity, appWxId, appSecret);
//        wxHandler.addToSocialSDK();
//        wxHandler.showCompressToast(false);//取消超过32k提示
//
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appWxId, appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//        wxCircleHandler.showCompressToast(false);//取消超过32k提示
//
//        String appId = "1105310925";//1104721294
//        String appKey = "zJAWN9Emnoj72D7Y";
//        // 添加QQ支持, 并且设置QQ分享内容的target url
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, appId, appKey);
//        qqSsoHandler.setTargetUrl(currentUrl);
//        qqSsoHandler.addToSocialSDK();
//
//        // 添加QZone平台
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, appId, appKey);
//        qZoneSsoHandler.addToSocialSDK();
//    }


    /**TODO
     * 友盟分享功能封装（移除暂时不用的默认平台，排序，启动分享弹框）
     */
//    public static void startPlatform(Activity mActivity) {
//        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE);//平台排序
//        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA, SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);//移除暂时不用的默认平台
//        mController.openShare(mActivity, false);
//    }

    /**TODO
     * 设置分享内容
     *
     * @param activity
     * @param title
     * @param url
     * @param content
     * @param imgUrl
     */
//    public static void setShareContent(Activity activity, String title, String url, String content, String imgUrl) {
//        UMImage urlImage = new UMImage(activity, imgUrl);//暂时设置的本地图片
//        WeiXinShareContent weixinContent = new WeiXinShareContent();
//        weixinContent.setShareContent(content);
//        weixinContent.setTitle(title);
//        weixinContent.setTargetUrl(url);
//        weixinContent.setShareMedia(urlImage);//如果没有图片，微信分享是不会有链接打开的
//        mController.setShareMedia(weixinContent);
//
//        // 设置朋友圈分享的内容
//        CircleShareContent circleMedia = new CircleShareContent();
//        circleMedia.setShareContent(content);
//
//        circleMedia.setTitle(title);
//        circleMedia.setShareMedia(urlImage);
//        // circleMedia.setShareMedia(uMusic);
//        // circleMedia.setShareMedia(video);
//        circleMedia.setTargetUrl(url);
//        mController.setShareMedia(circleMedia);
//
//
//        // 设置QQ空间分享内容
//        QZoneShareContent qzone = new QZoneShareContent();
//        qzone.setShareContent(content);
//        qzone.setTargetUrl(url);//需要用户打开的，产品所在的url
//        qzone.setTitle(title);
//        qzone.setShareMedia(urlImage);
//        // qzone.setShareMedia(uMusic);
//        mController.setShareMedia(qzone);
//
//
//        QQShareContent qqShareContent = new QQShareContent();
//        qqShareContent.setShareContent(content);
//        qqShareContent.setTitle(title);
//        qqShareContent.setShareMedia(urlImage);
//        qqShareContent.setTargetUrl(url);
//        mController.setShareMedia(qqShareContent);
//    }

    /**TODO
     * 分享回调，商品分享时调用
     */
//    static ShareCallBackLister shareCallBackLister;
//
//    public static void ShareCallBack(Activity activity, String pcode) {
//        if (shareCallBackLister == null) {
//            shareCallBackLister = new ShareCallBackLister();
//        }
//        mController.unregisterListener(shareCallBackLister);
//        shareCallBackLister.setContextAndPcode(activity, pcode);
//        mController.openShare(activity, shareCallBackLister);
//    }


    /**
     * inputStream装成String类型
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * 获取应用当前版本号
     *
     * @param c
     * @return
     */
    public static int getAppVersion(Context c) {
        int ret = 1;
        PackageInfo info;
        try {
            info = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            // 当前应用的版本名称
            String versionName = info.versionName;
            // 当前版本的版本号
            int versionCode = info.versionCode;
            // 当前版本的包名
            String packageNames = info.packageName;
            ret = versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**TODO
     * 获取百度地图缩放级别
     *
     * @return
     */
//    public static int getMaplevel(MapView mapView) {
//        int m = 16;//默认级别16，和地图初始化时级别一样
//        if (mapView != null) {
//            m = mapView.getMapLevel();
//        }
//        switch (m) {
//            case 20:
//                m = 19;
//                break;
//            case 50:
//                m = 18;
//                break;
//            case 100:
//                m = 17;
//                break;
//            case 200:
//                m = 16;
//                break;
//            case 500:
//                m = 15;
//                break;
//            case 1000:
//                m = 14;
//                break;
//            case 2000:
//                m = 13;
//                break;
//            case 5000:
//                m = 12;
//                break;
//            case 10000:
//                m = 11;
//                break;
//            case 20000:
//                m = 10;
//                break;
//            case 25000:
//                m = 9;
//                break;
//            case 50000:
//                m = 8;
//                break;
//            case 100000:
//                m = 7;
//                break;
//            case 200000:
//                m = 6;
//                break;
//            case 500000:
//                m = 5;
//                break;
//            case 1000000:
//                m = 4;
//                break;
//            case 2000000:
//                m = 3;
//                break;
//            case 5000000:
//                m = 2;
//                break;
//            case 10000000:
//                m = 1;
//                break;
//        }
//        return m;
//    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context mContext, float dpValue) {

        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp转化为px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context mContext, float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
