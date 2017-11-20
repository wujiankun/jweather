package com.wjk.jweather.weather.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wjk.jweather.R;
import com.wjk.jweather.about.AboutMeActivity;
import com.wjk.jweather.base.BaseActivity;
import com.wjk.jweather.db.WeatherDataParseBean;
import com.wjk.jweather.util.NetUtil;
import com.wjk.jweather.weather.adapter.MutiItemsAdapter;
import com.wjk.jweather.weather.adapter.UsualCityAdapter;
import com.wjk.jweather.weather.bean.airbeen.AirNowCity;
import com.wjk.jweather.db.BaseAreaParseBean;
import com.wjk.jweather.db.UsualCity;
import com.wjk.jweather.listener.CityChangeListener;
import com.wjk.jweather.location.LocateSelectActivity;
import com.wjk.jweather.util.MyConst;
import com.wjk.jweather.util.JsonUtil;
import com.wjk.jweather.weather.bean.weatherbeen.DailyForecast;
import com.wjk.jweather.weather.bean.weatherbeen.Heweather6;
import com.wjk.jweather.weather.bean.weatherbeen.Hourly;
import com.wjk.jweather.weather.bean.weatherbeen.LifestyleMap;
import com.wjk.jweather.weather.presenter.WeatherPresenter;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeatherActivity extends BaseActivity implements WeatherPresenter.OnUiListener {

    private LinearLayout weatherLayout;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private RecyclerView hourlyLayout;
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
    private ImageView iv_weather_ico;
    private View cv_hourly_wrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = new WeatherPresenter(this);
        mWeatherId = getIntent().getStringExtra("weather_id");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_weather;
    }

    @Override
    protected void findViews() {
        sr_pull_fresh = findViewById(R.id.sr_pull_fresh);
        weatherLayout = findViewById(R.id.ll_weather_layout);
        titleUpdateTime = findViewById(R.id.tv_update_time);
        degreeText = findViewById(R.id.tv_degree);
        iv_weather_ico = findViewById(R.id.iv_weather_ico);
        weatherInfoText = findViewById(R.id.tv_weather_info);
        forecastLayout = findViewById(R.id.ll_forecast_layout);
        hourlyLayout = findViewById(R.id.rv_hourly_layout);
        cv_hourly_wrapper = findViewById(R.id.cv_hourly_wrapper);
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
        setRetryClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loadData(mWeatherId);
            }
        });
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
                initData();
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                }
                //切换默认城市
                switchDefaultCity(city);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mUsualCityAdapter);
    }

    private void switchDefaultCity(BaseAreaParseBean county) {
        ContentValues values = new ContentValues();
        values.put("isLoveCity",0);
        //查询出默认城市的id然后修改为非默认
        List<UsualCity> usualCities = DataSupport.where("isLoveCity=?", "1")
                .find(UsualCity.class);
        if(usualCities.size()>0){
            DataSupport.update(UsualCity.class,values,usualCities.get(0).getId());
        }
        List<UsualCity> temp2 = DataSupport.where("areaCode=?", county.getAreaCode())
                .find(UsualCity.class);
        if(temp2.size()>0){
            values.put("isLoveCity",1);
            DataSupport.update(UsualCity.class,values,temp2.get(0).getId());
        }
        mUsualCityAdapter.setDataList(DataSupport.findAll(UsualCity.class));
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
        if (NetUtil.getAPNType(this) > 0) {
            showLoading();
            presenter.loadData(mWeatherId);
        } else {
            WeatherDataParseBean bean = presenter.getDataByWeatherId(mWeatherId, MyConst.COMMON_WEATHER_URL);
            if (bean != null) {
                com.wjk.jweather.weather.bean.weatherbeen.Heweather6 weather
                        = JsonUtil.handleWeatherResponse(bean.getContentStr());
                showWeatherInfo(weather);
                bean = presenter.getDataByWeatherId(mWeatherId, MyConst.AIR_QUALITY_URL);
                if (bean != null) {
                    com.wjk.jweather.weather.bean.airbeen.Heweather6 aiq6 = JsonUtil.handleAiqResponse(bean.getContentStr());
                    showAqi(aiq6);
                }
                showNoNetTip();
            }else{
                //无网无缓存
                showGetWeatherFail("咋回事儿啊？");
                showNetError();
            }
        }
    }

    /**
     * 提示用户当前无网络连接
     */
    private void showNoNetTip() {
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
            case R.id.menu_about:
                intent = new Intent(this, AboutMeActivity.class);
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
        } else if (state.contains("多云")) {
            bgId = R.mipmap.cloudy;
        } else if (state.contains("雾")) {
            bgId = R.mipmap.frog;
        } else if (state.contains("雨")) {
            bgId = R.mipmap.rainy;
        } else if (state.contains("雪")) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hour > 17) {
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
        setTitle(weather.getBasic().getLocation() + " " + weather.getBasic().getParentCity());
        titleUpdateTime.setText("更新时间：" + weather.getUpdate().getLoc());
        degreeText.setText(weather.getNow().getTmp() + "℃");
        weatherInfoText.setText(weather.getNow().getCondTxt());
        setBg(weather.getNow().getCondTxt());
        nowWind.setText(weather.getNow().getWindDir() + "-" + weather.getNow().getWindSc());
        String condCode = weather.getNow().getCondCode();
        Glide.with(this).load("file:///android_asset/ico/"+condCode+".png").into(iv_weather_ico);


        List<Hourly> hourly = weather.getHourly();
        if (hourly != null&&hourly.size()>0) {
            MutiItemsAdapter adapter = new MutiItemsAdapter(this);
            RecyclerView.LayoutManager layoutManager =
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
            hourlyLayout.setLayoutManager(layoutManager);
            hourlyLayout.setAdapter(adapter);
            adapter.setDataList(hourly);
            cv_hourly_wrapper.setVisibility(View.VISIBLE);
        }else{
            cv_hourly_wrapper.setVisibility(View.GONE);
        }
        for (int i = 0; i < weather.getLifestyle().size(); i++) {
            String type = weather.getLifestyle().get(i).getType();
            String typeName = LifestyleMap.styleVales.get(type);
            String text = typeName + "：" + weather.getLifestyle().get(i).getBrf() + "\n" + weather.getLifestyle().get(i).getTxt();
            TextView itemView = (TextView) lifeStyleLayout.getChildAt(i + 1);
            itemView.setText(text);
        }
        weatherLayout.setVisibility(View.VISIBLE);


        forecastLayout.removeAllViews();
        for (DailyForecast forecast : weather.getDailyForecast()) {
            View inflate = LayoutInflater.from(this)
                    .inflate(R.layout.layout_weather_forecast_item,
                            forecastLayout, false);
            TextView date = inflate.findViewById(R.id.tv_date);
            ImageView iv_weather_ico1 = inflate.findViewById(R.id.iv_weather_ico1);
            ImageView iv_weather_ico2 = inflate.findViewById(R.id.iv_weather_ico2);
            TextView temp = inflate.findViewById(R.id.tv_temp);
            TextView wind = inflate.findViewById(R.id.tv_wind);
            Date theDate = forecast.getDate();
            String dateText = generateDateText(theDate);
            date.setText(dateText);
            //info.setText(forecast.getCondTxtD() + "-" + forecast.getCondTxtN());
            temp.setText(forecast.getTmpMin() + "℃" + "~" + forecast.getTmpMax() + "℃");
            String windDir = forecast.getWindDir();
            windDir = windDir.replace("无持续风向","")+" ";
            wind.setText(windDir+forecast.getWindSc());
            Glide.with(this).load("file:///android_asset/ico/"+forecast.getCondCodeD()+".png").into(iv_weather_ico1);
            Glide.with(this).load("file:///android_asset/ico/"+forecast.getCondCodeN()+".png").into(iv_weather_ico2);
            forecastLayout.addView(inflate);
        }


        if (sr_pull_fresh.isRefreshing()) {
            sr_pull_fresh.setRefreshing(false);
            Toast.makeText(WeatherActivity.this,
                    "更新成功！", Toast.LENGTH_SHORT).show();

        }
        hideLoading();
    }

    private String generateDateText(Date theDate) {
        String dateTxt = (theDate.getMonth()+1) + "-" + theDate.getDate()+" ";
        int month = theDate.getMonth();
        int date = theDate.getDate();
        int day = theDate.getDay();
        Date now = new Date();
        int nowMonth = now.getMonth();
        int nowDate = now.getDate();

        if(month==nowMonth&&date==nowDate){
            dateTxt = "今天   ";
        }else if(month==nowMonth&&date==nowDate+1){
            dateTxt = "明天   ";
        }
        return dateTxt+MyConst.weekTxt[day];
    }

    @Override
    public void showGetWeatherFail(String msg) {
        sr_pull_fresh.setRefreshing(false);
        weatherLayout.setVisibility(View.GONE);
        if(msg.contains("解析")){
            showNoData();
        }else{
            showNetError();
        }
    }

    @Override
    public void showAqi(com.wjk.jweather.weather.bean.airbeen.Heweather6 aiqObj) {
        AirNowCity airNow = aiqObj.getAirNowCity();
        aqiText.setText(airNow.getAqi());
        pm25Text.setText(airNow.getPm25());
        pm10.setText(airNow.getPm10());
        airQuality.setText(airNow.getQlty());
        weatherAiqLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAqiFail(String msg) {
        weatherAiqLayout.setVisibility(View.GONE);
        if(msg.contains("解析")){
        }
    }

    @Override
    protected boolean isTransStatusBar() {
        return true;
    }

    @Override
    public boolean isLightStatusBar() {
        return false;
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
