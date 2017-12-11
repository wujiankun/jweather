package com.wjk.jweather.weather.bean.weatherbeen;

import com.wjk.jweather.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wujiankun on 2017/11/7.
 */

public class LifestyleMap {
    public static Map<String,String> styleVales = new HashMap<>();
    public static Map<String,Integer> styleImages = new HashMap<>();
    static {
        styleVales.put("comf","舒适度");
        styleVales.put("cw","洗车");
        styleVales.put("drsg","穿衣");
        styleVales.put("flu","感冒");
        styleVales.put("sport","运动");
        styleVales.put("trav","旅游");
        styleVales.put("uv","紫外线");
        styleVales.put("air","空气质量");

        styleImages.put("comf", R.mipmap.life_suggest_comfort);
        styleImages.put("cw",R.mipmap.life_suggest_car_wash);
        styleImages.put("drsg",R.mipmap.life_suggest_cloth);
        styleImages.put("flu",R.mipmap.life_suggest_flue);
        styleImages.put("sport",R.mipmap.life_suggest_sport);
        styleImages.put("trav",R.mipmap.life_suggest_travel);
        styleImages.put("uv",R.mipmap.life_suggest_uv);
        styleImages.put("air",R.mipmap.life_suggest_air);
    }
}
