/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.weather.bean.airbeen;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Auto-generated: 2017-11-08 15:3:56
 */
public class Heweather6 {


    private Basic basic;
    private Update update;
    private String status;
    @JSONField(name="air_now_city")
    private AirNowCity airNowCity;
    @JSONField(name="air_now_station")
    private List<AirNowStation> airNowStation;
    public void setBasic(Basic basic) {
        this.basic = basic;
    }
    public Basic getBasic() {
        return basic;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }
    public Update getUpdate() {
        return update;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setAirNowCity(AirNowCity airNowCity) {
        this.airNowCity = airNowCity;
    }
    public AirNowCity getAirNowCity() {
        return airNowCity;
    }

    public void setAirNowStation(List<AirNowStation> airNowStation) {
        this.airNowStation = airNowStation;
    }
    public List<AirNowStation> getAirNowStation() {
        return airNowStation;
    }

}