package com.wjk.jweather;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wjk.jweather.fragment.ChooseAreaFragment;

public class MainActivity extends AppCompatActivity implements ChooseAreaFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
