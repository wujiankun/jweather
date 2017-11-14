package com.wjk.jweather.view;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wjk.jweather.R;
import com.wjk.jweather.view.dummy.DummyContent;
import com.wjk.jweather.view.temp.TxtReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //init();
    }



    private void init() {
        ViewPager viewPager = findViewById(R.id.vp_frg_container);
        List<Fragment> fragments = new ArrayList<>();
        for(int i=0;i<4;i++){
            ItemFragment itemFragment = ItemFragment.newInstance(i);
            fragments.add(itemFragment);
        }
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),fragments));
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
