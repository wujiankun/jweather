package com.wjk.jweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wujiankun on 2017/10/16.
 * 各种出行建议
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CarWash carWash;
    public Sport sport;

    public DressSuggestion dressSuggestion;
    public Uv uv;

    public class Comfort{
        public String brf;
        @SerializedName("txt")
        public String info;
    }
    public class CarWash{
        public String brf;
        @SerializedName("txt")
        public String info;
    }
    public class Sport{
        public String brf;
        @SerializedName("txt")
        public String info;
    }

    public class DressSuggestion{
        public String brf;
        @SerializedName("txt")
        public String info;
    }

    public class Uv{
        public String brf;
        @SerializedName("txt")
        public String info;
    }
}
