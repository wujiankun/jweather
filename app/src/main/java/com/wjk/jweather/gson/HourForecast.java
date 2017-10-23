package com.wjk.jweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wujiankun on 2017/10/16.
 * 每小时的天气信息
 */

public class HourForecast {
    public String date;
    @SerializedName("tmp")
    public String tempreature;
    @SerializedName("cond")
    public More more;
    @SerializedName("wind")
    public Wind wind;

    public class More {
        @SerializedName("txt")
        public String info;
        public String code;
    }

    public class Wind {
        @SerializedName("dir")
        public String direction;
        @SerializedName("sc")
        public String level;
        @SerializedName("spd")
        public String speed;
    }
}
