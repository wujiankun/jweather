package com.wjk.jweather.db;

/**
 * Created by wujiankun on 2017/11/14.
 *
 * parse data from city-list txt file
 *
 */

public class AreaParseBean extends BaseAreaParseBean {
    private CityParseBean cityParseBean;
    public CityParseBean getCityParseBean() {
        return cityParseBean;
    }

    public void setCityParseBean(CityParseBean cityParseBean) {
        this.cityParseBean = cityParseBean;
    }

    public void initFromStrArr(String[] s) {
        setAreaCode(s[0]);
        setAreaEN(s[1]);
        setAreaCN(s[2]);
        setCountryCode(s[3]);
        setCountryEN(s[4]);
        setCountryCN(s[5]);
        setProvinceEN(s[6]);
        setProvinceCN(s[7]);
        setParentAreaEN(s[8]);
        setParentAreaCN(s[9]);
        setLatitude(Double.parseDouble(s[10]));
        setLongitude(Double.parseDouble(s[11]));
    }
}
