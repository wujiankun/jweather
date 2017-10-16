package com.wjk.jweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wujiankun on 2017/10/16.
 * AQI：aqi 与 pm25
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
        public String co;
        public String no2;
        public String o3;
        public String pm10;
        @SerializedName("qlty")
        public String quality;
        public String so2;
    }
}
