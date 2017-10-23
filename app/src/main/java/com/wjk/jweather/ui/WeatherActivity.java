package com.wjk.jweather.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wjk.jweather.R;
import com.wjk.jweather.db.UsualCity;
import com.wjk.jweather.gson.Forecast;
import com.wjk.jweather.gson.HourForecast;
import com.wjk.jweather.gson.Weather;
import com.wjk.jweather.util.GsonUtil;
import com.wjk.jweather.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView weatherLayout;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private LinearLayout hourlyLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private TextView airSugText;
    private TextView drsgText;
    private TextView fluText;
    private TextView travText;
    private TextView uvText;
    private TextView pm10;
    private TextView airQuality;
    private TextView nowWind;


    private ImageView imageBg;
    private SwipeRefreshLayout sr_pull_fresh;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private UsualCityAdapter mUsualCityAdapter;

    private String mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mWeatherId = getIntent().getStringExtra("weather_id");

        initToolBar();
        initViews();
        initSelectedCityView();
    }

    private void initSelectedCityView() {
        RecyclerView recyclerView = findViewById(R.id.rv_my_city_list);
        final List<UsualCity> cities = DataSupport.findAll(UsualCity.class);
        mUsualCityAdapter = new UsualCityAdapter(cities, new CityChangeListener() {
            @Override
            public void onCityChange(UsualCity city) {
                mWeatherId = city.getWeatherId();
                requestWeather(mWeatherId);
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawers();
                }

                ContentValues values = new ContentValues();
                values.put("isLoveCity",0);
                DataSupport.updateAll(UsualCity.class,values,"isLoveCity=?","1");

                city.setLoveCity(1);
                city.save();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setOrientation();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mUsualCityAdapter);
    }

    private void initToolBar() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
        }
    }

    private void initViews() {
        sr_pull_fresh = findViewById(R.id.sr_pull_fresh);
        weatherLayout = findViewById(R.id.sv_weather_layout);
        titleUpdateTime = findViewById(R.id.tv_update_time);
        degreeText = findViewById(R.id.tv_degree);
        weatherInfoText = findViewById(R.id.tv_weather_info);
        forecastLayout = findViewById(R.id.ll_forecast_layout);
        hourlyLayout = findViewById(R.id.ll_hourly_layout);
        aqiText = findViewById(R.id.tv_aqi);
        pm25Text = findViewById(R.id.tv_pm25);
        comfortText = findViewById(R.id.tv_comfort);
        carWashText = findViewById(R.id.tv_car_wash);
        sportText = findViewById(R.id.tv_sport);
        airSugText = findViewById(R.id.tv_air);
        drsgText = findViewById(R.id.tv_drsg);
        fluText = findViewById(R.id.tv_flu);
        travText = findViewById(R.id.tv_trav);
        uvText = findViewById(R.id.tv_uv);
        pm10 = findViewById(R.id.tv_pm10);
        airQuality = findViewById(R.id.tv_air_quality);
        nowWind = findViewById(R.id.tv_now_wind);

        imageBg = findViewById(R.id.iv_bg_pic);
        loadDataAndShow();
        sr_pull_fresh.setColorSchemeResources(R.color.colorPrimary);
        sr_pull_fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
    }

    private void loadDataAndShow() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString(mWeatherId, null);
        if (weatherString != null) {
            Weather weather = GsonUtil.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        String imageBgKey = prefs.getString("imageBgKey", null);
        String todayImageKey = new Date().getDate() + getIntent().getStringExtra("weather_id");
        if (imageBgKey != null && imageBgKey.equals(todayImageKey)) {
            String imageBgUrl = prefs.getString("imageBg", null);
            if (imageBgUrl != null) {
                Glide.with(this).load(imageBgUrl).into(imageBg);
            } else {
                loadImageBg();
            }
        } else {
            loadImageBg();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_add_city:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("add_city", 1);
                startActivity(intent);
                //startActivityForResult(intent,1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu,menu);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        boolean fromChooseArea = intent.getBooleanExtra("from_choose_area", false);
        if(fromChooseArea){
            mUsualCityAdapter.setDataList(DataSupport.findAll(UsualCity.class));
            mWeatherId = intent.getStringExtra("weather_id");
            loadDataAndShow();
        }
        super.onNewIntent(intent);
    }

    private void requestWeather(final String weatherId) {
        String url = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=13545e3a0d9b4ea0be767bb12e0707e5";
        String url5 = "https://free-api.heweather.com/v5/weather?city=" + weatherId + "&key=13545e3a0d9b4ea0be767bb12e0707e5";
        HttpUtil.sendRequest(url5, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,
                                "request weather info failed..", Toast.LENGTH_SHORT).show();
                        sr_pull_fresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseText = response.body().string();
                final Weather weather = GsonUtil.handleWeatherResponse(responseText);
                if (weather != null && "ok".equals(weather.status)) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences
                            (WeatherActivity.this).edit();
                    editor.putString(weatherId, responseText);
                    editor.apply();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            showWeatherInfo(weather);
                            if (sr_pull_fresh.isRefreshing()) {
                                Toast.makeText(WeatherActivity.this,
                                        "更新成功！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(WeatherActivity.this,weather.status,
                                    Toast.LENGTH_SHORT).show();
                        }
                        sr_pull_fresh.setRefreshing(false);
                    }
                });
            }
        });
        loadImageBg();
    }

    private void setToolBarTitle(String title){
        ActionBar bar = getSupportActionBar();
        if(bar!=null){
            bar.setTitle(title);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showWeatherInfo(Weather weather) {
        setToolBarTitle(weather.basic.cityName);
        titleUpdateTime.setText("更新时间:" + weather.basic.update.updateTime.split(" ")[1]);
        degreeText.setText(weather.now.temperature + "℃");
        weatherInfoText.setText(weather.now.more.info);
        nowWind.setText(weather.now.wind.direction+"-"+weather.now.wind.level);

        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View inflate = LayoutInflater.from(this)
                    .inflate(R.layout.layout_weather_forecast_item,
                            forecastLayout, false);
            TextView date = inflate.findViewById(R.id.tv_date);
            TextView info = inflate.findViewById(R.id.tv_info);
            TextView temp = inflate.findViewById(R.id.tv_temp);
            TextView wind = inflate.findViewById(R.id.tv_wind);
            date.setText(forecast.date);
            info.setText(forecast.more.txt_n+"-"+forecast.more.txt_d);
            temp.setText(forecast.tempreature.min+ "℃"+"-"+forecast.tempreature.max+ "℃");
            wind.setText(forecast.wind.direction+"-"+forecast.wind.level);
            forecastLayout.addView(inflate);
        }

        hourlyLayout.removeAllViews();
        for(HourForecast forecast:weather.hourlyList){
            View inflate = LayoutInflater.from(this).inflate(R.layout.layout_weather_forecast_item,
                    forecastLayout, false);
            TextView date = inflate.findViewById(R.id.tv_date);
            TextView info = inflate.findViewById(R.id.tv_info);
            TextView temp = inflate.findViewById(R.id.tv_temp);
            TextView wind = inflate.findViewById(R.id.tv_wind);
            date.setText(forecast.date.split(" ")[1]);
            info.setText(forecast.more.info);
            temp.setText(forecast.tempreature+ "℃");
            wind.setText(forecast.wind.direction+"-"+forecast.wind.level);
            hourlyLayout.addView(inflate);
        }

        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
            pm10.setText(weather.aqi.city.pm10);
            airQuality.setText(weather.aqi.city.quality);
        } else {
            aqiText.setText("暂无");
            pm25Text.setText("暂无");
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.brf+"\n"+weather.suggestion.comfort.info;
        String carWash = "洗车指数："  + weather.suggestion.carWash.brf+"\n"+ weather.suggestion.carWash.info;
        String sport = "运动建议："  + weather.suggestion.sport.brf+"\n"+ weather.suggestion.sport.info;
        String drsg = "穿衣建议："+weather.suggestion.dressSuggestion.brf+"\n"+ weather.suggestion.dressSuggestion.info;
        String flu = "感冒指数："+weather.suggestion.flu.brf+"\n"+ weather.suggestion.flu.info;
        String trav = "旅游指数："+weather.suggestion.trav.brf+"\n"+ weather.suggestion.trav.info;
        String uv = "紫外线："+weather.suggestion.uv.brf+"\n"+ weather.suggestion.uv.info;
        String airSug = "空气质量："+weather.suggestion.air.brf+"\n"+ weather.suggestion.air.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        drsgText.setText(drsg);
        fluText.setText(flu);
        travText.setText(trav);
        uvText.setText(uv);
        airSugText.setText(airSug);


        weatherLayout.setVisibility(View.VISIBLE);
    }

    private void loadImageBg() {
        HttpUtil.sendRequest("http://guolin.tech/api/bing_pic", new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String url = response.body().string();
                if (!TextUtils.isEmpty(url)) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences
                            (WeatherActivity.this).edit();
                    editor.putString("imageBg", url);
                    String imageBgKey = new Date().getDate() + getIntent().getStringExtra("weather_id");
                    editor.putString("imageBgKey", imageBgKey);
                    editor.apply();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WeatherActivity.this).load(url).into(imageBg);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}
