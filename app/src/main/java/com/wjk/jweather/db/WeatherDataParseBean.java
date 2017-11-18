package com.wjk.jweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by m on 2017/11/18.
 *
 */

public class WeatherDataParseBean extends DataSupport {
    private String key;
    private String contentStr;

    public WeatherDataParseBean(String weatherUrl, String resultStr) {
        key = weatherUrl;
        contentStr = resultStr;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }
}
