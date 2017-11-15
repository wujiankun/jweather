package com.wjk.jweather.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiankun on 2017/10/13.
 * the javabean of province,which extends DataSupport for LitePal use.
 * parse data from city-list txt file
 */

public class ProvinceParseBean extends DataSupport {
    private int id;//id是每个实体类都应该有的字段，此类中，不参与业务逻辑
   /*城市/地区编码	英文	中文	国家代码	国家英文	国家中文	省英文	省中文	所属上级市英文	 所属上级市中文	纬度 	经度*/

    private String areaCode;
    private String countryCode;
    private String countryEN;
    private String countryCN;
    private String provinceEN;
    private String provinceCN;
    private List<CityParseBean> cities = new ArrayList<>();

    public List<CityParseBean> getCities() {
        return cities;
    }

    public void setCities(List<CityParseBean> cities) {
        this.cities = cities;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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
}
