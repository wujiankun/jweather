package com.wjk.jweather.util;

import android.content.Context;

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

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
