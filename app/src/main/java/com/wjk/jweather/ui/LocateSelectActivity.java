package com.wjk.jweather.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wjk.jweather.R;

public class LocateSelectActivity extends AppCompatActivity implements ChooseAreaFragment.OnFragmentInteractionListener {

    private BaseFragment mCurrentFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_select);
    }

    @Override
    public void onFragmentSelect(BaseFragment fragment) {
        mCurrentFrag = fragment;
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFrag == null || !mCurrentFrag.handleBackPress()) {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
