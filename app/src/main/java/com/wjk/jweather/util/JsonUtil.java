package com.wjk.jweather.util;

import com.alibaba.fastjson.JSON;
import com.wjk.jweather.weather.bean.airbeen.AirRootBean;

import java.util.List;

/**
 * Created by wujiankun on 2017/10/13.
 * 对返回的json数据解析为对应的java类并通过litepal的特性添加到本地数据库中
 */

public class JsonUtil {


    public static com.wjk.jweather.weather.bean.weatherbeen.Heweather6 handleWeatherResponse(String response){
        try{
            com.wjk.jweather.weather.bean.weatherbeen.WeatherBean jsonDataObj = JSON.parseObject(response, com.wjk.jweather.weather.bean.weatherbeen.WeatherBean.class);
            List<com.wjk.jweather.weather.bean.weatherbeen.Heweather6> heweather6 = jsonDataObj.getHeweather6();
            if(heweather6!=null&&heweather6.size()>0){
                return heweather6.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static com.wjk.jweather.weather.bean.airbeen.Heweather6 handleAiqResponse(String response){
        try{
            AirRootBean jsonDataObj = JSON.parseObject(response, AirRootBean.class);
            List<com.wjk.jweather.weather.bean.airbeen.Heweather6> heweather6 = jsonDataObj.getHeweather6();
            if(heweather6!=null&&heweather6.size()>0){
                return heweather6.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
