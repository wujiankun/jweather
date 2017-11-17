/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.weather.bean.airbeen;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
/**
 * Auto-generated: 2017-11-08 15:3:56
 */
public class AirNow {

    @JSONField(name="air_city")
    private AirNowCity airCity;
    @JSONField(name="air_station")
    private List<AirNowStation> airStation;
    public void setAirCity(AirNowCity airCity) {
         this.airCity = airCity;
     }
     public AirNowCity getAirCity() {
         return airCity;
     }

    public void setAirStation(List<AirNowStation> airStation) {
         this.airStation = airStation;
     }
     public List<AirNowStation> getAirStation() {
         return airStation;
     }

}