package com.wjk.jweather.listener;

import android.os.Bundle;

import javax.xml.transform.Result;

/**
 * Created by wujiankun on 2017/11/17.
 *
 */

public interface OnNetCallBack {
    void onPreExecute(int var1);

    void networkCallBack(int var1, Bundle var2, Result var3);

    void onCancelled(int var1);
}
