/**
  * Copyright 2017 aTool.org 
  */
package com.wjk.jweather.airbeen;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * Auto-generated: 2017-11-08 15:3:56
 */
public class Basic {
    private String cid;
    private String location;
    @JSONField(name="parent_city")
    private String parentCity;
    @JSONField(name="admin_area")
    private String adminArea;
    private String cnty;
    private String lat;
    private String lon;
    private String tz;
    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getCid() {
        return cid;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }

    public void setParentCity(String parentCity) {
        this.parentCity = parentCity;
    }
    public String getParentCity() {
        return parentCity;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }
    public String getAdminArea() {
        return adminArea;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }
    public String getCnty() {
        return cnty;
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

    public void setTz(String tz) {
        this.tz = tz;
    }
    public String getTz() {
        return tz;
    }

}