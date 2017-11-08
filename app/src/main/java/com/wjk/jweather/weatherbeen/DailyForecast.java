/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.weatherbeen;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
/**
 * Auto-generated: 2017-11-07 17:34:23
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class DailyForecast {

    @JSONField(name="cond_code_d")
    private String condCodeD;
    @JSONField(name="cond_code_n")
    private String condCodeN;
    @JSONField(name="cond_txt_d")
    private String condTxtD;
    @JSONField(name="cond_txt_n")
    private String condTxtN;
    private Date date;
    private String hum;
    private String mr;
    private String ms;
    private String pcpn;
    private String pop;
    private String pres;
    private String sr;
    private String ss;
    @JSONField(name="tmp_max")
    private String tmpMax;
    @JSONField(name="tmp_min")
    private String tmpMin;
    @JSONField(name="uv_index")
    private String uvIndex;
    private String vis;
    @JSONField(name="wind_deg")
    private String windDeg;
    @JSONField(name="wind_dir")
    private String windDir;
    @JSONField(name="wind_sc")
    private String windSc;
    @JSONField(name="wind_spd")
    private String windSpd;
    public void setCondCodeD(String condCodeD) {
         this.condCodeD = condCodeD;
     }
     public String getCondCodeD() {
         return condCodeD;
     }

    public void setCondCodeN(String condCodeN) {
         this.condCodeN = condCodeN;
     }
     public String getCondCodeN() {
         return condCodeN;
     }

    public void setCondTxtD(String condTxtD) {
         this.condTxtD = condTxtD;
     }
     public String getCondTxtD() {
         return condTxtD;
     }

    public void setCondTxtN(String condTxtN) {
         this.condTxtN = condTxtN;
     }
     public String getCondTxtN() {
         return condTxtN;
     }

    public void setDate(Date date) {
         this.date = date;
     }
     public Date getDate() {
         return date;
     }

    public void setHum(String hum) {
         this.hum = hum;
     }
     public String getHum() {
         return hum;
     }

    public void setMr(String mr) {
         this.mr = mr;
     }
     public String getMr() {
         return mr;
     }

    public void setMs(String ms) {
         this.ms = ms;
     }
     public String getMs() {
         return ms;
     }

    public void setPcpn(String pcpn) {
         this.pcpn = pcpn;
     }
     public String getPcpn() {
         return pcpn;
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

    public void setSr(String sr) {
         this.sr = sr;
     }
     public String getSr() {
         return sr;
     }

    public void setSs(String ss) {
         this.ss = ss;
     }
     public String getSs() {
         return ss;
     }

    public void setTmpMax(String tmpMax) {
         this.tmpMax = tmpMax;
     }
     public String getTmpMax() {
         return tmpMax;
     }

    public void setTmpMin(String tmpMin) {
         this.tmpMin = tmpMin;
     }
     public String getTmpMin() {
         return tmpMin;
     }

    public void setUvIndex(String uvIndex) {
         this.uvIndex = uvIndex;
     }
     public String getUvIndex() {
         return uvIndex;
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