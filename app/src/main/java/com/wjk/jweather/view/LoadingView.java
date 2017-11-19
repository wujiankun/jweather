package com.wjk.jweather.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wjk.jweather.R;


/**
 * 加载界面
 */
public class LoadingView extends RelativeLayout {

    /**
     * UI
     */
    // 暂无数据stub
    private ViewStub noDataStub;
    // 网络错误stub
    private ViewStub netErrStub;

    // loading
    private RelativeLayout loadingLayout;
    private TextView loadingTv;

    // ###########暂无数据#############
    private LinearLayout noDataLayout;
    // 暂无数据的文本
    private TextView noDataTitleTv = null;
    private ImageView noDataImg = null;

    // ###########网络错误#############
    // 网络错误
    private LinearLayout netErrLayout;
    // 网络错误
    private TextView netErrorTv = null;
    private ImageView netErrorImg = null;


    // 重试的点击事件
    private OnClickListener mRetryListener;

    /**
     * 数据
     */
    // 暂无数据的id
    private int noDataHint = R.string.load_no_data;

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.app_loading_layout, this);
        // loading
        loadingLayout = findViewById(R.id.loading_layout);
        loadingTv = (TextView) findViewById(R.id.tv_loading);
        // 网络错误
        netErrStub = (ViewStub) findViewById(R.id.net_err_view_stub);
        // 暂无数据
        noDataStub = (ViewStub) findViewById(R.id.no_data_view_stub);
    }

    /**
     * 加载中
     */
    public void loading() {
        setVisibility(VISIBLE);
        if (noDataLayout != null) {
            noDataLayout.setVisibility(GONE);
        }
        if (netErrLayout != null) {
            netErrLayout.setVisibility(GONE);
        }
        //
        loadingLayout.setVisibility(VISIBLE);
    }

    /**
     * 无数据
     */
    public void noData() {
        noData(noDataHint, -1);
    }

    /**
     * 无数据
     */
    public void noData(@StringRes int title, @DrawableRes int drawableId) {
        setVisibility(VISIBLE);
        // loading 隐藏
        loadingLayout.setVisibility(GONE);
        // 显示暂无数据
        if (noDataLayout != null) {
            noDataLayout.setVisibility(VISIBLE);
        } else {
            noDataLayout = (LinearLayout) noDataStub.inflate();
            noDataTitleTv = (TextView) noDataLayout.findViewById(R.id.tv_no_data);
            noDataImg = (ImageView) noDataLayout.findViewById(R.id.loading_no_data_img);
        }
        // 网络错误隐藏
        if (netErrLayout != null) {
            netErrLayout.setVisibility(GONE);
        }
        /**
         * 更新UI数据
         */
        // 文本内容
        if (title > 0) {
            noDataTitleTv.setText(title);
        }
    }

    /**
     * 网络错误，点击重新加载
     */
    public void netErr() {
        setVisibility(VISIBLE);
        loadingLayout.setVisibility(GONE);
        if (noDataLayout != null) {
            noDataLayout.setVisibility(GONE);
        }
        if (netErrLayout != null) {
            netErrLayout.setVisibility(VISIBLE);
        } else {
            netErrLayout = (LinearLayout) netErrStub.inflate();
            netErrLayout.setOnClickListener(mRetryListener);
            //
            netErrorTv = (TextView) netErrLayout.findViewById(R.id.tv_error);
            netErrorImg = (ImageView) netErrLayout.findViewById(R.id.loading_net_error_img);
        }
    }

    public boolean isLoading() {
        return getVisibility() == VISIBLE && loadingLayout.getVisibility() == VISIBLE;
    }

    /**
     * 点击重新加载监听
     */
    public void setRetryListener(OnClickListener listener) {
        mRetryListener = listener;
    }

    /**
     * 隐藏
     */
    public void hide() {
        setVisibility(GONE);
    }

    public void setNoDataHint(int resId) {
        noDataHint = resId;
    }


}