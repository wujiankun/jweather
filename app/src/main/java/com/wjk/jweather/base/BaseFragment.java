package com.wjk.jweather.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wjk.jweather.R;
import com.wjk.jweather.view.ILoadingView;
import com.wjk.jweather.view.LoadingView;


public abstract class BaseFragment extends Fragment implements ILoadingView{

    protected LoadingView mLoadingView;

    protected View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutResId(), container, false);
            initDefaultViews(mRootView);
            initViews();
            initData();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract void initViews();

    protected abstract void initData();

    protected void initDefaultViews(View view) {
        //mLoadingView = (LoadingView) view.findViewById(R.id.loading_view);
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
        waitDialog = new ProgressDialog(getActivity());
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

    /**
     * 重试点击
     */
    protected void setRetryClick(View.OnClickListener onClickListener) {
        if (mLoadingView != null)
            mLoadingView.setRetryListener(onClickListener);
    }

    @Override
    public void onDestroyView() {
        stopWaiting();
        super.onDestroyView();
    }


    public abstract boolean handleBackPress();
}
