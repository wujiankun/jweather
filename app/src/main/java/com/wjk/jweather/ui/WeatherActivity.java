package com.wjk.jweather.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.wjk.jweather.gson.Forecast;
import com.wjk.jweather.gson.Weather;
import com.wjk.jweather.util.GsonUtil;
import com.wjk.jweather.util.HttpUtil;

import java.io.IOException;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView imageBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if(Build.VERSION.SDK_INT>=21){//为了让界面与状态栏相融合
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        setContentView(R.layout.activity_weather);
        initViews();
    }

    private void initViews() {
        weatherLayout = findViewById(R.id.sv_weather_layout);
        titleCity = findViewById(R.id.tv_title_city);
        titleUpdateTime = findViewById(R.id.tv_update_time);
        degreeText = findViewById(R.id.tv_degree);
        weatherInfoText = findViewById(R.id.tv_weather_info);
        forecastLayout = findViewById(R.id.ll_forecast_layout);
        aqiText = findViewById(R.id.tv_aqi);
        pm25Text = findViewById(R.id.tv_pm25);
        comfortText = findViewById(R.id.tv_comfort);
        carWashText = findViewById(R.id.tv_car_wash);
        sportText = findViewById(R.id.tv_sport);
        imageBg = findViewById(R.id.iv_bg_pic);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherId = getIntent().getStringExtra("weather_id");
        String weatherString = prefs.getString(weatherId, null);
        if (weatherString != null) {
            Weather weather = GsonUtil.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        String imageBgKey = prefs.getString("imageBgKey", null);
        String todayImageKey = new Date().getDate()+getIntent().getStringExtra("weather_id");
        if(imageBgKey!=null&&imageBgKey.equals(todayImageKey)){
            String imageBgUrl = prefs.getString("imageBg", null);
            if (imageBgUrl != null) {
                Glide.with(this).load(imageBgUrl).into(imageBg);
            } else {
                loadImageBg();
            }
        }else{
            loadImageBg();
        }
    }

    private void requestWeather(String weatherId) {
        String url = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=14356967dfa3405db4cf2bd8bcc4ed51";
        String url5 = "https://free-api.heweather.com/v5/weather?city=" + weatherId + "&key=14356967dfa3405db4cf2bd8bcc4ed51";
        HttpUtil.sendRequest(url5, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,
                                "request weather info failed..", Toast.LENGTH_SHORT).show();
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
                    editor.putString("weatherId", responseText);
                    editor.apply();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this,
                                    "request weather info failed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        loadImageBg();
    }

    @SuppressLint("SetTextI18n")
    private void showWeatherInfo(Weather weather) {
        titleCity.setText(weather.basic.cityName);
        titleUpdateTime.setText("更新时间:"+weather.basic.update.updateTime.split(" ")[1]);
        degreeText.setText(weather.now.temperature + "℃");
        weatherInfoText.setText(weather.now.more.info);

        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View inflate = LayoutInflater.from(this)
                    .inflate(R.layout.layout_weather_forecast_item,
                            forecastLayout, false);
            TextView date = inflate.findViewById(R.id.tv_date);
            TextView info = inflate.findViewById(R.id.tv_info);
            TextView max = inflate.findViewById(R.id.tv_max);
            TextView min = inflate.findViewById(R.id.tv_min);
            date.setText(forecast.date);
            info.setText(forecast.more.info);
            max.setText(forecast.tempreature.max);
            min.setText(forecast.tempreature.min);
            forecastLayout.addView(inflate);
        }

        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        } else {
            aqiText.setText("暂无");
            pm25Text.setText("暂无");
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);

        findViewById(R.id.ib_add_city).setOnClickListener(this);
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
                    String imageBgKey = new Date().getDate()+getIntent().getStringExtra("weather_id");
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
        switch (view.getId()){
            case R.id.ib_add_city:
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("add_city",1);
                startActivity(intent);
                break;
        }
    }
}
