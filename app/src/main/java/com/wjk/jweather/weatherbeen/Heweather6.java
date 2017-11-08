/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.weatherbeen;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
/**
 * Auto-generated: 2017-11-07 17:34:23
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class Heweather6 {

    private Basic basic;
    private Update update;
    private String status;
    private Now now;
    @JSONField(name="daily_forecast")
    private List<DailyForecast> dailyForecast;
    private List<Hourly> hourly;
    private List<Lifestyle> lifestyle;
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

    public void setNow(Now now) {
         this.now = now;
     }
     public Now getNow() {
         return now;
     }

    public void setDailyForecast(List<DailyForecast> dailyForecast) {
         this.dailyForecast = dailyForecast;
     }
     public List<DailyForecast> getDailyForecast() {
         return dailyForecast;
     }

    public void setHourly(List<Hourly> hourly) {
         this.hourly = hourly;
     }
     public List<Hourly> getHourly() {
         return hourly;
     }

    public void setLifestyle(List<Lifestyle> lifestyle) {
         this.lifestyle = lifestyle;
     }
     public List<Lifestyle> getLifestyle() {
         return lifestyle;
     }

}