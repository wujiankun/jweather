/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.airbeen;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
/**
 * Auto-generated: 2017-11-08 15:3:56
 */
public class AirRootBean {

    @JSONField(name="HeWeather6")
    private List<Heweather6> heweather6;
    public void setHeweather6(List<Heweather6> heweather6) {
         this.heweather6 = heweather6;
     }
     public List<Heweather6> getHeweather6() {
         return heweather6;
     }

}