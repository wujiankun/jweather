package com.wjk.jweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wujiankun on 2017/10/16.
 * 总的实体类
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
