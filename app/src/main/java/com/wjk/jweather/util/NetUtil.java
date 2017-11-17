package com.wjk.jweather.util;

/**
 * Created by wujiankun on 2017/11/17.
 */

public class NetUtil {
    public static boolean isNetAvailable(){
        Runtime runtime = Runtime.getRuntime();
        try {
            Process p = runtime.exec("ping -c 3 www.baidu.com");
            int ret = p.waitFor();
            return ret==0;
        } catch (Exception e) {
            return false;
        }
    }
}
