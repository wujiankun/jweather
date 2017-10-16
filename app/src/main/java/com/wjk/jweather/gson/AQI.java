package com.wjk.jweather.gson;

/**
 * Created by wujiankun on 2017/10/16.
 * AQI：aqi 与 pm25
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
