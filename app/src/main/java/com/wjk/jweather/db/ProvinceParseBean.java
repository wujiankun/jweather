package com.wjk.jweather.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiankun on 2017/10/13.
 * the javabean of province,which extends DataSupport for LitePal use.
 * parse data from city-list txt file
 */

public class ProvinceParseBean extends BaseAreaParseBean {

    private List<CityParseBean> cities = new ArrayList<>();

    public List<CityParseBean> getCities() {
        return cities;
    }

    public void setCities(List<CityParseBean> cities) {
        this.cities = cities;
    }
}
