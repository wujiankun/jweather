/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.airbeen;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * Auto-generated: 2017-11-08 15:3:56
 */
public class AirStation {

    @JSONField(name="air_sta")
    private String airSta;
    private String aqi;
    private String asid;
    private String co;
    private String lat;
    private String lon;
    private String main;
    private String no2;
    private String o3;
    private String pm10;
    private String pm25;
    @JSONField(name="pub_time")
    private String pubTime;
    private String qlty;
    private String so2;
    public void setAirSta(String airSta) {
         this.airSta = airSta;
     }
     public String getAirSta() {
         return airSta;
     }

    public void setAqi(String aqi) {
         this.aqi = aqi;
     }
     public String getAqi() {
         return aqi;
     }

    public void setAsid(String asid) {
         this.asid = asid;
     }
     public String getAsid() {
         return asid;
     }

    public void setCo(String co) {
         this.co = co;
     }
     public String getCo() {
         return co;
     }

    public void setLat(String lat) {
         this.lat = lat;
     }
     public String getLat() {
         return lat;
     }

    public void setLon(String lon) {
         this.lon = lon;
     }
     public String getLon() {
         return lon;
     }

    public void setMain(String main) {
         this.main = main;
     }
     public String getMain() {
         return main;
     }

    public void setNo2(String no2) {
         this.no2 = no2;
     }
     public String getNo2() {
         return no2;
     }

    public void setO3(String o3) {
         this.o3 = o3;
     }
     public String getO3() {
         return o3;
     }

    public void setPm10(String pm10) {
         this.pm10 = pm10;
     }
     public String getPm10() {
         return pm10;
     }

    public void setPm25(String pm25) {
         this.pm25 = pm25;
     }
     public String getPm25() {
         return pm25;
     }

    public void setPubTime(String pubTime) {
         this.pubTime = pubTime;
     }
     public String getPubTime() {
         return pubTime;
     }

    public void setQlty(String qlty) {
         this.qlty = qlty;
     }
     public String getQlty() {
         return qlty;
     }

    public void setSo2(String so2) {
         this.so2 = so2;
     }
     public String getSo2() {
         return so2;
     }

}