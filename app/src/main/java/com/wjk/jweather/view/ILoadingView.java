package com.wjk.jweather.view;

/**
 * 基础页面接口
 * Created by liuguansheng on 2017/9/6.
 */

public interface ILoadingView {

    /**
     * 显示加载动画
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示无数据
     */
    void showNoData();

    /**
     * 显示网络错误，modify 对网络异常在 BaseActivity 和 BaseFragment 统一处理
     */
    void showNetError();
}
