package com.wjk.jweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wujiankun on 2017/10/16.
 * 单日的天气信息
 */

public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Temperature tempreature;
    @SerializedName("cond")
    public More more;
    @SerializedName("wind")
    public Wind wind;


    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }

    public class Wind{
        @SerializedName("dir")
        public String direction;
        @SerializedName("sc")
        public String level;
        @SerializedName("spd")
        public String speed;
    }
}
