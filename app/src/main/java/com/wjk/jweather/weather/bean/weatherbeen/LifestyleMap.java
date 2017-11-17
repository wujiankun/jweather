package com.wjk.jweather.weather.bean.weatherbeen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wujiankun on 2017/11/7.
 */

public class LifestyleMap {
    public static Map<String,String> styleVales = new HashMap<>();
    static {
        styleVales.put("comf","舒适度指数");
        styleVales.put("cw","洗车指数");
        styleVales.put("drsg","穿衣指数");
        styleVales.put("flu","感冒指数");
        styleVales.put("sport","运动指数");
        styleVales.put("trav","旅游指数");
        styleVales.put("uv","紫外线指数");
        styleVales.put("air","空气质量指数");
    }
}
