package com.wjk.jweather.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wjk.jweather.R;
import com.wjk.jweather.gson.Forecast;
import com.wjk.jweather.gson.Weather;
import com.wjk.jweather.util.GsonUtil;
import com.wjk.jweather.util.HttpUtil;

import java.io.IOException;

public class WeatherActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherId = getIntent().getStringExtra("weather_id");
        String weatherString = prefs.getString(weatherId,null);
        if(weatherString!=null){
            Weather weather = GsonUtil.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    private void requestWeather(String weatherId) {
        String url = "http://guolin.tech/api/weather?cityid="+weatherId+"&key=14356967dfa3405db4cf2bd8bcc4ed51";
        String url5 = "https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=14356967dfa3405db4cf2bd8bcc4ed51";
        HttpUtil.sendRequest(url5, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,
                                "request weather info failed..",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseText = response.body().string();
                final Weather weather = GsonUtil.handleWeatherResponse(responseText);
                if(weather!=null&&"ok".equals(weather.status)){
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences
                            (WeatherActivity.this).edit();
                    editor.putString("weatherId",responseText);
                    editor.apply();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null&&"ok".equals(weather.status)){
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,
                                    "request weather info failed..",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showWeatherInfo(Weather weather) {
        titleCity.setText(weather.basic.cityName);
        titleUpdateTime.setText(weather.basic.update.updateTime.split(" ")[1]);
        degreeText.setText(weather.now.temperature+"℃");
        weatherInfoText.setText(weather.now.more.info);

        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
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

        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }else{
            aqiText.setText("暂无");
            pm25Text.setText("暂无");
        }

        String comfort = "舒适度："+weather.suggestion.comfort.info;
        String carWash = "洗车指数："+weather.suggestion.carWash.info;
        String sport = "运动建议："+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);
    }
}
