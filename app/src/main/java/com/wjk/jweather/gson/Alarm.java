package com.wjk.jweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wujiankun on 2017/10/16.
 *"level": "蓝色",
 *"stat": "预警中",
 *"title": "山东省青岛市气象台发布大风蓝色预警",
 *"txt": "青岛市气象台2016年08月29日15时24分继续发布大风蓝色预警信号：预计今天下午到明天，我市北风风力海上6到7级阵风9级，陆地4到5阵风7级，请注意防范。",
 *"type": "大风"
 *}
 */

public class Alarm {
    String level;
    @SerializedName("stat")
    String state;
    String title;
    @SerializedName("txt")
    String content;
    String type;
}
