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
public class WeatherBean {

    @JSONField(name="HeWeather6")
    private List<Heweather6> heweather6;
    public void setHeweather6(List<Heweather6> heweather6) {
         this.heweather6 = heweather6;
     }
     public List<Heweather6> getHeweather6() {
         return heweather6;
     }

}