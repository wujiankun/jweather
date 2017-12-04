package com.wjk.jweather.util;

/**
 * Created by wujiankun on 2017/12/4.
 * 通用工具类
 */

public class CommonUtil {
    public static String makeAreaTile(String location,String parentArea,String province,String append){
        StringBuilder sb = new StringBuilder();
        if (location.equals(parentArea)) {
            if(location.equals(province)){
                sb.append(location);
            }else{
                sb.append(province);sb.append(append);sb.append(location);
            }
        } else {
            sb.append(parentArea);sb.append(append);sb.append(location);
        }
        return sb.toString();
    }
}
