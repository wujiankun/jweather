package com.wjk.jweather.weather.model;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wjk.jweather.BuildConfig;
import com.wjk.jweather.db.WeatherDataParseBean;
import com.wjk.jweather.util.MyConst;
import com.wjk.jweather.util.HttpUtil;
import com.wjk.jweather.util.JsonUtil;
import com.wjk.jweather.weather.bean.weatherbeen.Heweather6;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

/**
 * Created by wujiankun on 2017/11/17.
 *
 */

public class WeatherModle implements Callback {

    private Listener mListener;
    private String weatherUrl,airUrl;

    public WeatherModle(Listener listener){
        mListener = listener;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        if(mListener==null){
            return;
        }
        String from = request.urlString();
        if(from.equals(weatherUrl)){
            mListener.onGetWeatherInfoFail(e.getMessage());
        }else if(from.equals(airUrl)){
            mListener.onGetAqiFail(e.getMessage());
        }
    }

    @Override
    public void onResponse(Response response) throws IOException {
        if(mListener==null){
            return;
        }
        String resultStr = response.body().string();
        String from = response.request().urlString();
        if(from.equals(weatherUrl)){
            Heweather6 weather = JsonUtil.handleWeatherResponse(resultStr);
            if (weather != null && "ok".equals(weather.getStatus())){
                mListener.onGetWeatherInfoSucess(weather);
                saveToDb(weatherUrl,resultStr);
            }else{
                mListener.onGetWeatherInfoFail("解析天气数据失败");
            }
        }else if(from.equals(airUrl)){
            com.wjk.jweather.weather.bean.airbeen.Heweather6 aiqObj= JsonUtil.handleAiqResponse(resultStr);
            if (aiqObj == null || aiqObj.getAirNowCity() == null) {
                mListener.onGetAqiFail("解析空气质量数据失败");
            }else{
                mListener.onGetAqiSucess(aiqObj);
                saveToDb(airUrl,resultStr);
            }
        }
    }

    private void saveToDb(String weatherUrl, String resultStr) {
        new WeatherDataParseBean(weatherUrl,resultStr).save();
    }

    public WeatherDataParseBean getDataByWeatherId(String weatherId,String urlInConst){
        String key = urlInConst+"?location=" + weatherId + "&key=" + BuildConfig.appKey;
        List<WeatherDataParseBean> parseBeans = DataSupport.where("key = ?", key).find(WeatherDataParseBean.class);
        if(parseBeans.size()>0){
            return parseBeans.get(0);
        }else{
            return null;
        }
    }

    public void requestCommonWeather(String weatherId){
        weatherUrl = MyConst.COMMON_WEATHER_URL + "?location=" + weatherId + "&key=" + BuildConfig.appKey;
        /*HashMap<String,String> params = new HashMap<>();
        params.put("location",weatherId);
        params.put("key",BuildConfig.appKey);
        try {
            String signature = ParamEncode.getSignature(params, BuildConfig.APPLICATION_ID);
            url5 = "https://free-api.heweather.com/s6/weather?sign="+signature;
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        HttpUtil.sendRequest(weatherUrl,this);
    }

    public void requestAqi(String weatherId){
        airUrl = MyConst.AIR_QUALITY_URL + "?location=" + weatherId + "&key=" + BuildConfig.appKey;
        HttpUtil.sendRequest(airUrl,this);
    }

    public interface Listener {
        void onGetWeatherInfoSucess(Heweather6 weatherInfo);
        void onGetWeatherInfoFail(String msg);
        void onGetAqiSucess(com.wjk.jweather.weather.bean.airbeen.Heweather6 aqiInfo);
        void onGetAqiFail(String msg);
    }
}
