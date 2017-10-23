package com.wjk.jweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wujiankun on 2017/10/16.
 * 气温与天气txt
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public Cond more;
    public class Cond{
        @SerializedName("txt")
        public String info;
        public String code;
    }
    @SerializedName("wind")
    public Forecast.Wind wind;
}
