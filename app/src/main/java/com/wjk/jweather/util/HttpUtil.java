package com.wjk.jweather.util;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * Created by wujiankun on 2017/10/13.
 * 封装okhttp相关api
 */

public class HttpUtil {
    /**
     * 发送网络请求，底层用okhttp
     * @param url url
     * @param callback callback
     */
    public static void sendRequest(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
