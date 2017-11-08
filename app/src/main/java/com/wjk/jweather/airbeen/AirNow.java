/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.airbeen;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
/**
 * Auto-generated: 2017-11-08 15:3:56
 */
public class AirNow {

    @JSONField(name="air_city")
    private AirCity airCity;
    @JSONField(name="air_station")
    private List<AirStation> airStation;
    public void setAirCity(AirCity airCity) {
         this.airCity = airCity;
     }
     public AirCity getAirCity() {
         return airCity;
     }

    public void setAirStation(List<AirStation> airStation) {
         this.airStation = airStation;
     }
     public List<AirStation> getAirStation() {
         return airStation;
     }

}