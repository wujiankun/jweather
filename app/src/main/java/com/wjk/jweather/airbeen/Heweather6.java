/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.airbeen;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * Auto-generated: 2017-11-08 15:3:56
 */
public class Heweather6 {

    @JSONField(name="air_now")
    private AirNow airNow;
    private Basic basic;
    private String status;
    private Update update;
    public void setAirNow(AirNow airNow) {
         this.airNow = airNow;
     }
     public AirNow getAirNow() {
         return airNow;
     }

    public void setBasic(Basic basic) {
         this.basic = basic;
     }
     public Basic getBasic() {
         return basic;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setUpdate(Update update) {
         this.update = update;
     }
     public Update getUpdate() {
         return update;
     }

}