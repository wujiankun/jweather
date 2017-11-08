/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.weatherbeen;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * Auto-generated: 2017-11-07 17:34:23
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class Hourly {

    private String cloud;
    @JSONField(name="cond_code")
    private String condCode;
    @JSONField(name="cond_txt")
    private String condTxt;
    private String dew;
    private String hum;
    private String pop;
    private String pres;
    private String time;
    private String tmp;
    @JSONField(name="wind_deg")
    private String windDeg;
    @JSONField(name="wind_dir")
    private String windDir;
    @JSONField(name="wind_sc")
    private String windSc;
    @JSONField(name="wind_spd")
    private String windSpd;
    public void setCloud(String cloud) {
         this.cloud = cloud;
     }
     public String getCloud() {
         return cloud;
     }

    public void setCondCode(String condCode) {
         this.condCode = condCode;
     }
     public String getCondCode() {
         return condCode;
     }

    public void setCondTxt(String condTxt) {
         this.condTxt = condTxt;
     }
     public String getCondTxt() {
         return condTxt;
     }

    public void setDew(String dew) {
         this.dew = dew;
     }
     public String getDew() {
         return dew;
     }

    public void setHum(String hum) {
         this.hum = hum;
     }
     public String getHum() {
         return hum;
     }

    public void setPop(String pop) {
         this.pop = pop;
     }
     public String getPop() {
         return pop;
     }

    public void setPres(String pres) {
         this.pres = pres;
     }
     public String getPres() {
         return pres;
     }

    public void setTime(String time) {
         this.time = time;
     }
     public String getTime() {
         return time;
     }

    public void setTmp(String tmp) {
         this.tmp = tmp;
     }
     public String getTmp() {
         return tmp;
     }

    public void setWindDeg(String windDeg) {
         this.windDeg = windDeg;
     }
     public String getWindDeg() {
         return windDeg;
     }

    public void setWindDir(String windDir) {
         this.windDir = windDir;
     }
     public String getWindDir() {
         return windDir;
     }

    public void setWindSc(String windSc) {
         this.windSc = windSc;
     }
     public String getWindSc() {
         return windSc;
     }

    public void setWindSpd(String windSpd) {
         this.windSpd = windSpd;
     }
     public String getWindSpd() {
         return windSpd;
     }

}