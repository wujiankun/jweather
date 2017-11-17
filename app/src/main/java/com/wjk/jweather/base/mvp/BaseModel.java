package com.wjk.jweather.base.mvp;


import com.wjk.jweather.listener.OnNetCallBack;

/**
 * Created by liuguansheng on 2017/9/22.
 *
 */

public abstract class BaseModel implements IBaseModel, OnNetCallBack {

    @Override
    public void onPreExecute(int requestCode) {

    }

    @Override
    public void onCancelled(int requestCode) {

    }

    @Override
    public void onDestroy() {

    }
}
