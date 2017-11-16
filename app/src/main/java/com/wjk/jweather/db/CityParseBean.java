package com.wjk.jweather.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiankun on 2017/10/13.
 * the javabean of province,which extends DataSupport for LitePal use.
 * parse data from city-list txt file
 */

public class CityParseBean extends BaseAreaParseBean {

    private List<AreaParseBean> blocks = new ArrayList<>();
    private ProvinceParseBean provinceParseBean;
    public ProvinceParseBean getProvinceParseBean() {
        return provinceParseBean;
    }

    public void setProvinceParseBean(ProvinceParseBean provinceParseBean) {
        this.provinceParseBean = provinceParseBean;
    }
    public List<AreaParseBean> getBlocks() {
        return blocks;
    }
    public void setBlocks(List<AreaParseBean> blocks) {
        this.blocks = blocks;
    }
   }
