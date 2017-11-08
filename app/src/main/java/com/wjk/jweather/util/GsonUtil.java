package com.wjk.jweather.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.wjk.jweather.airbeen.AirRootBean;
import com.wjk.jweather.db.City;
import com.wjk.jweather.db.County;
import com.wjk.jweather.db.Province;
import com.wjk.jweather.weatherbeen.Heweather6;
import com.wjk.jweather.weatherbeen.WeatherBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by wujiankun on 2017/10/13.
 * 对返回的json数据解析为对应的java类并通过litepal的特性添加到本地数据库中
 */

public class GsonUtil {
    /**
     * 解析和处理返回的省级数据
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray array = new JSONArray(response);
                for(int i=0;i<array.length();i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    Province p = new Province();
                    p.setProvinceName(jsonObject.getString("name"));
                    p.setCode(jsonObject.getInt("id"));
                    p.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 解析和处理返回的市级数据
     * @param response
     * @return
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray array = new JSONArray(response);
                for(int i=0;i<array.length();i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    City city = new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCode(jsonObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理返回的地区级数据
     * @param response
     * @return
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray array = new JSONArray(response);
                for(int i=0;i<array.length();i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(jsonObject.getString("name"));
                    county.setCode(jsonObject.getInt("id"));
                    county.setCityId(cityId);
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Heweather6 handleWeatherResponse(String response){
        try{
            /*JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();*/
            WeatherBean jsonDataObj = JSON.parseObject(response, WeatherBean.class);
            List<Heweather6> heweather6 = jsonDataObj.getHeweather6();
            if(heweather6!=null&&heweather6.size()>0){
                return heweather6.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static com.wjk.jweather.airbeen.Heweather6 handleAiqResponse(String response){
        try{
            AirRootBean jsonDataObj = JSON.parseObject(response, AirRootBean.class);
            List<com.wjk.jweather.airbeen.Heweather6> heweather6 = jsonDataObj.getHeweather6();
            if(heweather6!=null&&heweather6.size()>0){
                return heweather6.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
