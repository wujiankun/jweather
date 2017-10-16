package com.wjk.jweather;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wjk.jweather.fragment.BaseFragment;
import com.wjk.jweather.fragment.ChooseAreaFragment;

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
