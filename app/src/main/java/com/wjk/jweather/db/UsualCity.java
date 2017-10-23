package com.wjk.jweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wujiankun on 2017/10/16.
 * 保存用户常用地点
 */

public class UsualCity extends DataSupport {
    private int id;
    private int isLoveCity;

    private String countyName;
    private String provinceName;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    private String weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int isLoveCity() {
        return isLoveCity;
    }

    public void setLoveCity(int loveCity) {
        isLoveCity = loveCity;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
