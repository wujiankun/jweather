package com.wjk.jweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by wujiankun on 2017/10/13.
 * the javabean of province,which extends DataSupport for LitePal use.
 */

public class Province extends DataSupport {
    private int id;//id是每个实体类都应该有的字段，此类中，不参与业务逻辑
    private String provinceName;
    private int code;//指代接口中返回的省id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
