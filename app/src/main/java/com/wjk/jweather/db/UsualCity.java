package com.wjk.jweather.db;

/**
 * Created by wujiankun on 2017/10/16.
 * 保存用户常用地点
 */

public class UsualCity extends BaseAreaParseBean {
    private int isLoveCity;
    public int isLoveCity() {
        return isLoveCity;
    }
    public void setLoveCity(int loveCity) {
        isLoveCity = loveCity;
    }
}
