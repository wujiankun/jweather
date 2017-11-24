package com.wjk.jweather.weather.presenter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.wjk.jweather.base.mvp.IBasePresenter;
import com.wjk.jweather.db.WeatherDataParseBean;
import com.wjk.jweather.weather.bean.weatherbeen.Heweather6;
import com.wjk.jweather.weather.model.WeatherModle;

/**
 * Created by wujiankun on 2017/11/17.
 *
 */

public class WeatherPresenter implements WeatherModle.Listener,IBasePresenter{

    private OnUiListener mListener;
    private WeatherModle mModle;
    private InnerHandler handler;
    private final int msg_show_weather_info = 1;
    private final int msg_show_aqi_info = 2;
    private final int msg_show_weather_fail = 3;
    private final int msg_show_aqi_fail = 4;

    public WeatherPresenter(OnUiListener listener){
        mListener = listener;
        mModle = new WeatherModle(this);
        handler = new InnerHandler();
    }

    public void loadData(String weatherId,String parentId){
        mModle.requestCommonWeather(weatherId);
        mModle.requestAqi(parentId);
    }

    public WeatherDataParseBean getDataByWeatherId(String weatherId,String urlInConst){
        return mModle.getDataByWeatherId(weatherId,urlInConst);
    }

    @Override
    public void onGetWeatherInfoSucess(Heweather6 weatherInfo) {
        Message msg = handler.obtainMessage(msg_show_weather_info);
        msg.obj = weatherInfo;
        handler.sendMessage(msg);
    }

    @Override
    public void onGetWeatherInfoFail(String msgStr) {
        Message msg = handler.obtainMessage(msg_show_weather_fail);
        msg.obj = msgStr;
        handler.sendMessage(msg);
    }

    @Override
    public void onGetAqiSucess(com.wjk.jweather.weather.bean.airbeen.Heweather6 aqiInfo) {
        Message msg = handler.obtainMessage(msg_show_aqi_info);
        msg.obj = aqiInfo;
        handler.sendMessage(msg);
    }

    @Override
    public void onGetAqiFail(String msgStr) {
        Message msg = handler.obtainMessage(msg_show_aqi_fail);
        msg.obj = msgStr;
        handler.sendMessage(msg);
    }

    @Override
    public void onDestroy() {
        if(handler!=null){
            handler.removeCallbacks(null);
            handler = null;
        }
    }

    @SuppressLint("HandlerLeak")
    private  class InnerHandler extends Handler {
        InnerHandler(){
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case msg_show_aqi_fail:
                    mListener.showAqiFail((String) msg.obj);
                    break;
                case msg_show_weather_fail:
                    mListener.showGetWeatherFail((String) msg.obj);
                    break;
                case msg_show_aqi_info:
                    mListener.showAqi((com.wjk.jweather.weather.bean.airbeen.Heweather6) msg.obj);
                    break;
                case msg_show_weather_info:
                    mListener.showWeatherInfo((Heweather6)msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public interface OnUiListener{
        void showWeatherInfo(Heweather6 weatherInfo);
        void showGetWeatherFail(String msg);
        void showAqi(com.wjk.jweather.weather.bean.airbeen.Heweather6 aqiInfo);
        void showAqiFail(String msg);
    }
}
