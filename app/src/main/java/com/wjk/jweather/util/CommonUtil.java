package com.wjk.jweather.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wjk.jweather.R;

import org.litepal.util.LogUtil;

import java.io.IOException;
import java.util.Calendar;

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

    public static void showWeatherIcoDay(Context context,String weatherCode,ImageView imageView){
        Glide.with(context).load("file:///android_asset/ico_new/" + weatherCode + ".png").into(imageView);
    }
    public static void showWeatherIcoNight(Context context,String weatherCode,ImageView imageView){
        if(isAssetFileExists(context,weatherCode+"1.png")){
            Glide.with(context).load("file:///android_asset/ico_new/" + weatherCode + "1.png").into(imageView);
        }else{
            showWeatherIcoDay(context,weatherCode,imageView);
        }
    }

    /**
     * 判断assets文件夹下的文件是否存在
     *
     * @return false 不存在    true 存在
     */
    public static boolean isAssetFileExists(Context context,String filename) {
        AssetManager assetManager = context.getAssets();
        try {
            String[] names = assetManager.list("ico_new");
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(filename.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean isNigthNow(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int month = calendar.get(Calendar.MONTH) + 1;
        if(month>=11||month<=3){
            return hour >= 17;
        }else{
            return hour >= 18;
        }
    }

    public static boolean isNigthNow(int hour){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        if(month>=11||month<=3){
            return hour >= 17;
        }else{
            return hour >= 18;
        }
    }
}
