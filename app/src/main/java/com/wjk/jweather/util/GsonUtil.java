package com.wjk.jweather.util;

import android.text.TextUtils;

import com.wjk.jweather.db.City;
import com.wjk.jweather.db.County;
import com.wjk.jweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
}
