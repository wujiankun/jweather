package com.wjk.jweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wujiankun on 2017/10/16.
 * 天气基本信息：城市名称，天气id，更新时间
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("prov")
    public String provinceName;
    @SerializedName("id")
    public String weatherId;
    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
