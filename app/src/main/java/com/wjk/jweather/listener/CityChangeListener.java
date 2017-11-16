package com.wjk.jweather.listener;

import com.wjk.jweather.db.BaseAreaParseBean;

/**
 * Created by wujiankun on 2017/10/17.\
 *
 */

public interface CityChangeListener {
    void onCityChange(BaseAreaParseBean city, int position);
}
