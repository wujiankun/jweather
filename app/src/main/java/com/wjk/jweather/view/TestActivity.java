package com.wjk.jweather.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.wjk.jweather.R;
import com.wjk.jweather.view.dummy.DummyContent;

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
