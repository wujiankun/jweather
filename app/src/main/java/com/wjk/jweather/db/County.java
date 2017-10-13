package com.wjk.jweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wujiankun on 2017/10/13.
 * the javabean of city,which extends DataSupport for LitePal use.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private int code;
    private int cityId;
    private int weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }
}
