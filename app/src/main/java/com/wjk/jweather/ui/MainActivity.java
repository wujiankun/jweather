package com.wjk.jweather.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wjk.jweather.R;
import com.wjk.jweather.ui.BaseFragment;
import com.wjk.jweather.ui.ChooseAreaFragment;

public class MainActivity extends AppCompatActivity implements ChooseAreaFragment.OnFragmentInteractionListener{

    private BaseFragment mCurrentFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentSelect(BaseFragment fragment) {
        mCurrentFrag = fragment;
    }

    @Override
    public void onBackPressed() {
        if(mCurrentFrag==null||!mCurrentFrag.handleBackPress()){
            if(getFragmentManager().getBackStackEntryCount()==0){
                super.onBackPressed();
            }else{
                getFragmentManager().popBackStack();
            }
        }
    }
}
