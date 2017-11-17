package com.wjk.jweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wujiankun on 2017/11/14.
 * <p>
 * base parse data from city-list txt file
 */

public class BaseAreaParseBean extends DataSupport {
    protected int id;//id是每个实体类都应该有的字段，此类中，不参与业务逻辑

    /*城市/地区编码	英文	中文	国家代码	国家英文	国家中文	省英文	省中文	所属上级市英文	 所属上级市中文	纬度 	经度*/

    protected String areaCode;
    protected String areaEN;
    protected String areaCN;
    protected String countryCode;
    protected String countryEN;
    protected String countryCN;
    protected String provinceEN;
    protected String provinceCN;
    protected String parentAreaEN;
    protected String parentAreaCN;
    protected double latitude;
    protected double longitude;

    public void copyValueFrom(BaseAreaParseBean bean) {
        areaCode = bean.getAreaCode();
        areaEN = bean.getAreaEN();
        areaCN = bean.getAreaCN();
        countryCode = bean.getCountryCode();
        countryEN = bean.getCountryEN();
        countryCN = bean.getCountryCN();
        provinceEN = bean.getProvinceEN();
        provinceCN = bean.getProvinceCN();
        parentAreaEN = bean.getParentAreaEN();
        parentAreaCN = bean.getParentAreaCN();
        latitude = bean.getLatitude();
        longitude = bean.getLongitude();
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaEN() {
        return areaEN;
    }

    public void setAreaEN(String areaEN) {
        this.areaEN = areaEN;
    }

    public String getAreaCN() {
        return areaCN;
    }

    public void setAreaCN(String areaCN) {
        this.areaCN = areaCN;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryEN() {
        return countryEN;
    }

    public void setCountryEN(String countryEN) {
        this.countryEN = countryEN;
    }

    public String getCountryCN() {
        return countryCN;
    }

    public void setCountryCN(String countryCN) {
        this.countryCN = countryCN;
    }

    public String getProvinceEN() {
        return provinceEN;
    }

    public void setProvinceEN(String provinceEN) {
        this.provinceEN = provinceEN;
    }

    public String getProvinceCN() {
        return provinceCN;
    }

    public void setProvinceCN(String provinceCN) {
        this.provinceCN = provinceCN;
    }

    public String getParentAreaEN() {
        return parentAreaEN;
    }

    public void setParentAreaEN(String parentAreaEN) {
        this.parentAreaEN = parentAreaEN;
    }

    public String getParentAreaCN() {
        return parentAreaCN;
    }

    public void setParentAreaCN(String parentAreaCN) {
        this.parentAreaCN = parentAreaCN;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
