/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.weather.bean.weatherbeen;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Auto-generated: 2017-11-07 17:34:23
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class Now {

    private String cloud;
    @JSONField(name="cond_code")
    private String condCode;
    @JSONField(name="cond_txt")
    private String condTxt;
    private String fl;
    private String hum;
    private String pcpn;
    private String pres;
    private String tmp;
    private String vis;
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

    public void setFl(String fl) {
         this.fl = fl;
     }
     public String getFl() {
         return fl;
     }

    public void setHum(String hum) {
         this.hum = hum;
     }
     public String getHum() {
         return hum;
     }

    public void setPcpn(String pcpn) {
         this.pcpn = pcpn;
     }
     public String getPcpn() {
         return pcpn;
     }

    public void setPres(String pres) {
         this.pres = pres;
     }
     public String getPres() {
         return pres;
     }

    public void setTmp(String tmp) {
         this.tmp = tmp;
     }
     public String getTmp() {
         return tmp;
     }

    public void setVis(String vis) {
         this.vis = vis;
     }
     public String getVis() {
         return vis;
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