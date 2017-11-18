package com.wjk.jweather.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.wjk.jweather.R;
import com.wjk.jweather.util.StatusBarUtil;
import com.wjk.jweather.view.ILoadingView;
import com.wjk.jweather.view.LoadingView;

/**
 * 基础Activity
 */

public abstract class BaseActivity extends AppCompatActivity implements ILoadingView {

    protected Toolbar mToolbar;

    protected LoadingView mLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initStatusBar();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        initDefaultViews();
        findViews();
        initViews();
        initData();
    }


    @Override
    protected void onDestroy() {
        //
        stopWaiting();
        //
        super.onDestroy();
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract void findViews();

    protected abstract void initViews();

    protected abstract void initData();

    private void initDefaultViews() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (mToolbar != null) {
            initToolbar();
        }
        mLoadingView = (LoadingView) findViewById(R.id.loading_view);
    }

    /**
     * 初始化状态栏
     */
    public void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.StatusBarLightMode(this, isLightStatusBar());
            if (isTransStatusBar())
                StatusBarUtil.setTransStatusBar(this, isLightStatusBar());
        }
    }

    /**
     * 是否是浅色状态栏（深色文字）
     */
    public boolean isLightStatusBar() {
        return false;
    }

    /**
     * 是否是透明状态栏
     */
    protected boolean isTransStatusBar() {
        return true;
    }

    protected void initToolbar() {
        if (mToolbar == null) {
            throw new IllegalStateException("toolbar has not be found in layout,be sure you have define toolbar in the layout");
        } else {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void setTitle(String title) {
        setTitle(title, false);
    }

    public void setTitle(String title, boolean anim) {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(title);
        }
    }

    public String getTitleText(){
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            return bar.getTitle().toString();
        }else{
            return "";
        }
    }

    public void setTitleText(CharSequence title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(title);
    }


    /**
     * 等待弹框
     */
    private ProgressDialog waitDialog;

    public void showWaiting(int resId) {
        showWaiting(resId, true);
    }

    public void showWaiting(int resId, boolean cancelable) {
        stopWaiting();
        waitDialog = new ProgressDialog(this);
        waitDialog.setMessage(getString(resId));
        waitDialog.setCancelable(cancelable);
        waitDialog.show();
    }

    public void stopWaiting() {
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
        }
    }

    /**
     * loading
     */
    @Override
    public void showLoading() {
        if (mLoadingView != null)
            mLoadingView.loading();
    }

    @Override
    public void hideLoading() {
        if (mLoadingView != null)
            mLoadingView.hide();
    }

    @Override
    public void showNoData() {
        if (mLoadingView != null)
            mLoadingView.noData();
    }

    @Override
    public void showNetError() {
        if (mLoadingView != null)
            mLoadingView.netErr();
    }

    // 重试点击
    protected void setRetryClick(View.OnClickListener onClickListener) {
        if (mLoadingView != null)
            mLoadingView.setRetryListener(onClickListener);
    }

}
