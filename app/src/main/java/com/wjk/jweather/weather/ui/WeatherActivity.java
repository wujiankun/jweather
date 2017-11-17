package com.wjk.jweather.weather.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wjk.jweather.BuildConfig;
import com.wjk.jweather.R;
import com.wjk.jweather.base.BaseActivity;
import com.wjk.jweather.util.NetUtil;
import com.wjk.jweather.weather.adapter.UsualCityAdapter;
import com.wjk.jweather.weather.bean.airbeen.AirNowCity;
import com.wjk.jweather.db.BaseAreaParseBean;
import com.wjk.jweather.db.UsualCity;
import com.wjk.jweather.listener.CityChangeListener;
import com.wjk.jweather.location.LocateSelectActivity;
import com.wjk.jweather.util.ConstUrl;
import com.wjk.jweather.util.JsonUtil;
import com.wjk.jweather.util.HttpUtil;
import com.wjk.jweather.weather.bean.weatherbeen.DailyForecast;
import com.wjk.jweather.weather.bean.weatherbeen.Heweather6;
import com.wjk.jweather.weather.bean.weatherbeen.Hourly;
import com.wjk.jweather.weather.bean.weatherbeen.LifestyleMap;
import com.wjk.jweather.weather.presenter.WeatherPresenter;

import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeatherActivity extends BaseActivity implements WeatherPresenter.OnUiListener{

    private NestedScrollView weatherLayout;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private LinearLayout hourlyLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView pm10;
    private TextView airQuality;
    private TextView nowWind;
    private ImageView imageBg;
    private SwipeRefreshLayout sr_pull_fresh;
    private DrawerLayout drawerLayout;
    private UsualCityAdapter mUsualCityAdapter;
    private String mWeatherId;
    private LinearLayout lifeStyleLayout;
    private ViewGroup weatherAiqLayout;
    private WeatherPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = new WeatherPresenter(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_weather;
    }

    @Override
    protected void findViews() {
        sr_pull_fresh = findViewById(R.id.sr_pull_fresh);
        weatherLayout = findViewById(R.id.sv_weather_layout);
        titleUpdateTime = findViewById(R.id.tv_update_time);
        degreeText = findViewById(R.id.tv_degree);
        weatherInfoText = findViewById(R.id.tv_weather_info);
        forecastLayout = findViewById(R.id.ll_forecast_layout);
        hourlyLayout = findViewById(R.id.ll_hourly_layout);
        aqiText = findViewById(R.id.tv_aqi);
        pm25Text = findViewById(R.id.tv_pm25);
        pm10 = findViewById(R.id.tv_pm10);
        airQuality = findViewById(R.id.tv_air_quality);
        nowWind = findViewById(R.id.tv_now_wind);

        imageBg = findViewById(R.id.iv_bg_pic);
        lifeStyleLayout = findViewById(R.id.ll_life_style_layout);
        weatherAiqLayout = findViewById(R.id.fl_weather_aqi_layout);
        sr_pull_fresh.setColorSchemeResources(R.color.colorPrimary);
        sr_pull_fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadData(mWeatherId);
            }
        });
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    @Override
    protected void initViews() {
        initSelectedCityView();
    }

    /**
     * 初始化抽屉中已选择城市列表
     */
    private void initSelectedCityView() {
        RecyclerView recyclerView = findViewById(R.id.rv_my_city_list);
        final List<UsualCity> cities = DataSupport.findAll(UsualCity.class);
        mUsualCityAdapter = new UsualCityAdapter(cities, new CityChangeListener() {
            @Override
            public void onCityChange(BaseAreaParseBean city, int position) {
                mWeatherId = city.getAreaEN();
                presenter.loadData(mWeatherId);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mUsualCityAdapter);
    }

    @Override
    protected void initToolbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
        }
    }
    @Override
    protected void initData() {
        Log.e("wjk","init time:"+System.currentTimeMillis());
        mWeatherId = getIntent().getStringExtra("weather_id");
        if(NetUtil.isNetAvailable()){
            presenter.loadData(mWeatherId);
        }else{
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String weatherString = prefs.getString(mWeatherId, null);
            if (weatherString != null) {
                com.wjk.jweather.weather.bean.weatherbeen.Heweather6 weather = JsonUtil.handleWeatherResponse(weatherString);
                showWeatherInfo(weather);
            }
            String aiqStr = prefs.getString("aiq" + mWeatherId, null);
            if(aiqStr!=null){
                com.wjk.jweather.weather.bean.airbeen.Heweather6 aiq6 = JsonUtil.handleAiqResponse(aiqStr);
                showAqi(aiq6);
            }
            showNoNet();
        }
    }

    /**
     * 提示用户当前无网络连接
     */
    private void showNoNet() {
        Toast.makeText(WeatherActivity.this,
                "您的手机无法访问网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_add_city:
                Intent intent = new Intent(this, LocateSelectActivity.class);
                intent.putExtra("add_city", 1);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        boolean fromChooseArea = intent.getBooleanExtra("from_choose_area", false);
        if (fromChooseArea) {
            mUsualCityAdapter.setDataList(DataSupport.findAll(UsualCity.class));
            mWeatherId = intent.getStringExtra("weather_id");
            presenter.loadData(mWeatherId);
        }
        super.onNewIntent(intent);
    }

    private void setBg(String state) {
        int bgId = R.mipmap.default_bg;
        if (state.contains("晴")) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hour > 18) {
                bgId = R.mipmap.sunny_night;
            } else {
                bgId = R.mipmap.sunny;
            }
        } else if (state.equals("多云")) {
            bgId = R.mipmap.cloudy;
        } else if (state.equals("雾")) {
            bgId = R.mipmap.frog;
        } else if (state.equals("雨")) {
            bgId = R.mipmap.rainy;
        } else if (state.equals("雪")) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hour > 18) {
                bgId = R.mipmap.snow_night;
            } else {
                bgId = R.mipmap.snow;
            }
        }
        Glide.with(WeatherActivity.this).load(bgId).into(imageBg);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void showWeatherInfo(Heweather6 weather) {
        Log.e("wjk","show info time:"+System.currentTimeMillis());
        if (weather != null && "ok".equals(weather.getStatus())) {
            if (sr_pull_fresh.isRefreshing()) {
                Toast.makeText(WeatherActivity.this,
                        "更新成功！", Toast.LENGTH_SHORT).show();
                sr_pull_fresh.setRefreshing(false);
            }
        } else {
            Toast.makeText(WeatherActivity.this, weather.getStatus(),
                    Toast.LENGTH_SHORT).show();
            sr_pull_fresh.setRefreshing(false);
            return;
        }

        setTitle(weather.getBasic().getLocation() + " " + weather.getBasic().getParentCity());
        titleUpdateTime.setText("更新时间：" + weather.getUpdate().getLoc());
        degreeText.setText(weather.getNow().getTmp() + "℃");
        weatherInfoText.setText(weather.getNow().getCondTxt());
        setBg(weather.getNow().getCondTxt());
        nowWind.setText(weather.getNow().getWindDir() + "-" + weather.getNow().getWindSc());
        forecastLayout.removeAllViews();
        for (DailyForecast forecast : weather.getDailyForecast()) {
            View inflate = LayoutInflater.from(this)
                    .inflate(R.layout.layout_weather_forecast_item,
                            forecastLayout, false);
            TextView date = inflate.findViewById(R.id.tv_date);
            TextView info = inflate.findViewById(R.id.tv_info);
            TextView temp = inflate.findViewById(R.id.tv_temp);
            TextView wind = inflate.findViewById(R.id.tv_wind);
            Date theDate = forecast.getDate();
            date.setText(theDate.getMonth()+"-"+theDate.getDate()+" 周"+theDate.getDay());
            info.setText(forecast.getCondTxtD() + "-" + forecast.getCondTxtN());
            temp.setText(forecast.getTmpMin() + "℃" + "-" + forecast.getTmpMax() + "℃");
            wind.setText(forecast.getWindDir() + "-" + forecast.getWindSc());
            forecastLayout.addView(inflate);
        }
        hourlyLayout.removeAllViews();
        hourlyLayout.setVisibility(View.GONE);
        List<Hourly> hourly = weather.getHourly();
        if(hourly!=null){
            for (Hourly forecast : weather.getHourly()) {
                View inflate = LayoutInflater.from(this).inflate(R.layout.layout_weather_forecast_item,
                        forecastLayout, false);
                TextView date = inflate.findViewById(R.id.tv_date);
                TextView info = inflate.findViewById(R.id.tv_info);
                TextView temp = inflate.findViewById(R.id.tv_temp);
                TextView wind = inflate.findViewById(R.id.tv_wind);
                date.setText(forecast.getTime().split(" ")[1]);
                info.setText(forecast.getCondTxt());
                temp.setText(forecast.getTmp() + "℃");
                wind.setText(forecast.getWindDir() + "-" + forecast.getWindSc());
                hourlyLayout.addView(inflate);
            }
            hourlyLayout.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < weather.getLifestyle().size(); i++) {
            String type = weather.getLifestyle().get(i).getType();
            String typeName = LifestyleMap.styleVales.get(type);
            String text = typeName + "：" + weather.getLifestyle().get(i).getBrf() + "\n" + weather.getLifestyle().get(i).getTxt();
            TextView itemView = (TextView) lifeStyleLayout.getChildAt(i + 1);
            itemView.setText(text);
        }
        weatherLayout.setVisibility(View.VISIBLE);

        if ("ok".equals(weather.getStatus())) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences
                    (WeatherActivity.this).edit();
            editor.putString(mWeatherId, JSON.toJSONString(weather));
            editor.apply();
        }
    }

    @Override
    public void showGetWeatherFail(String msg) {
        Toast.makeText(WeatherActivity.this,
                "request weather info failed..", Toast.LENGTH_SHORT).show();
        sr_pull_fresh.setRefreshing(false);
        weatherLayout.setVisibility(View.INVISIBLE);
        setBg("");
    }

    @Override
    public void showAqi(com.wjk.jweather.weather.bean.airbeen.Heweather6 aiqObj) {
        if (aiqObj == null || aiqObj.getAirNowCity() == null) {
            weatherAiqLayout.setVisibility(View.GONE);
            return;
        }
        AirNowCity airNow = aiqObj.getAirNowCity();
        aqiText.setText(airNow.getAqi());
        pm25Text.setText(airNow.getPm25());
        pm10.setText(airNow.getPm10());
        airQuality.setText(airNow.getQlty());
        weatherAiqLayout.setVisibility(View.VISIBLE);

        if ("ok".equals(aiqObj.getStatus())) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences
                    (WeatherActivity.this).edit();
            editor.putString("aiq" + mWeatherId, JSON.toJSONString(aiqObj));
            editor.apply();
        }
    }

    @Override
    public void showAqiFail(String msg) {

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
