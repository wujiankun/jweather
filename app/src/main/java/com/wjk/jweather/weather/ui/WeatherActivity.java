package com.wjk.jweather.weather.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.wjk.jweather.db.BaseAreaParseBean;
import com.wjk.jweather.db.UsualCity;
import com.wjk.jweather.db.WeatherDataParseBean;
import com.wjk.jweather.listener.CityChangeListener;
import com.wjk.jweather.location.LocateSelectActivity;
import com.wjk.jweather.util.CommonUtil;
import com.wjk.jweather.util.JsonUtil;
import com.wjk.jweather.util.MyConst;
import com.wjk.jweather.util.NetUtil;
import com.wjk.jweather.view.CirclePanelView;
import com.wjk.jweather.view.TawerView;
import com.wjk.jweather.weather.adapter.MutiItemsAdapter;
import com.wjk.jweather.weather.adapter.UsualCityAdapter;
import com.wjk.jweather.weather.bean.airbeen.AirNowCity;
import com.wjk.jweather.weather.bean.weatherbeen.DailyForecast;
import com.wjk.jweather.weather.bean.weatherbeen.Heweather6;
import com.wjk.jweather.weather.bean.weatherbeen.Hourly;
import com.wjk.jweather.weather.bean.weatherbeen.Lifestyle;
import com.wjk.jweather.weather.bean.weatherbeen.LifestyleMap;
import com.wjk.jweather.weather.presenter.WeatherPresenter;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

public class WeatherActivity extends BaseActivity implements WeatherPresenter.OnUiListener {

    private LinearLayout weatherLayout;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private RecyclerView forecastLayout;
    private RecyclerView hourlyLayout;
    private TawerView pm25Text;
    private TawerView pm10;
    private TextView airQuality;
    private TextView nowWind;
    private ImageView imageBg;
    private SwipeRefreshLayout sr_pull_fresh;
    private DrawerLayout drawerLayout;
    private UsualCityAdapter mUsualCityAdapter;
    private String mWeatherId;
    private String mParentWeatherId;
    private GridLayout lifeStyleLayout;
    private ViewGroup weatherAiqLayout;
    private WeatherPresenter presenter;
    private ImageView iv_weather_ico;
    private View cv_hourly_wrapper;
    private TextView tv_forecast_title;
    private LinearLayout ll_daily_forecast_wrapper;
    private TawerView tv_air_co;
    private TawerView tv_so2;
    private TawerView tv_o3;
    private TawerView tv_no2;
    private CirclePanelView cp_api_panel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = new WeatherPresenter(this);
        mWeatherId = getIntent().getStringExtra("weather_id");
        mParentWeatherId = getIntent().getStringExtra("parent_id");
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
        ll_daily_forecast_wrapper = findViewById(R.id.ll_daily_forecast_wrapper);
        forecastLayout = findViewById(R.id.rv_forecast_layout);
        tv_forecast_title = findViewById(R.id.tv_forecast_title);
        hourlyLayout = findViewById(R.id.rv_hourly_layout);
        cv_hourly_wrapper = findViewById(R.id.cv_hourly_wrapper);

        pm25Text = findViewById(R.id.tv_pm25);
        pm10 = findViewById(R.id.tv_pm10);
        tv_air_co = findViewById(R.id.tv_air_co);
        tv_so2 = findViewById(R.id.tv_so2);
        tv_o3 = findViewById(R.id.tv_o3);
        tv_no2 = findViewById(R.id.tv_no2);
        airQuality = findViewById(R.id.tv_air_quality);
        cp_api_panel = findViewById(R.id.cp_api_panel);

        nowWind = findViewById(R.id.tv_now_wind);

        imageBg = findViewById(R.id.iv_bg_pic);
        lifeStyleLayout = findViewById(R.id.gl_life_style_layout);
        weatherAiqLayout = findViewById(R.id.ll_weather_aqi_layout);
        sr_pull_fresh.setColorSchemeResources(R.color.colorPrimary);
        sr_pull_fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadData(mWeatherId, mParentWeatherId);
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
                presenter.loadData(mWeatherId, mParentWeatherId);
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
                mWeatherId = city.getAreaCode();
                mParentWeatherId = city.getParentAreaCN();
                initData();
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                }
                //切换默认城市
                switchDefaultCity(city);
            }

            @Override
            public void onCityDelete(BaseAreaParseBean city, int position) {
                deleteCity(city);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mUsualCityAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void switchDefaultCity(BaseAreaParseBean county) {
        ContentValues values = new ContentValues();
        values.put("isLoveCity", 0);
        //查询出默认城市的id然后修改为非默认
        List<UsualCity> usualCities = DataSupport.where("isLoveCity=?", "1")
                .find(UsualCity.class);
        if (usualCities.size() > 0) {
            DataSupport.update(UsualCity.class, values, usualCities.get(0).getId());
        }
        List<UsualCity> temp2 = DataSupport.where("areaCode=?", county.getAreaCode())
                .find(UsualCity.class);
        if (temp2.size() > 0) {
            values.put("isLoveCity", 1);
            DataSupport.update(UsualCity.class, values, temp2.get(0).getId());
        }
        mUsualCityAdapter.setDataList(DataSupport.findAll(UsualCity.class));
    }

    private void deleteCity(BaseAreaParseBean county) {
        List<UsualCity> temp2 = DataSupport.where("areaCode=?", county.getAreaCode())
                .find(UsualCity.class);
        if (temp2.size() > 0) {
            final UsualCity usualCity = temp2.get(0);
            new AlertDialog.Builder(this)
                    .setTitle("删除地区")
                    .setMessage("您确定要删除" + usualCity.getAreaCN() + "吗？")
                    .setPositiveButton("删了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DataSupport.delete(UsualCity.class, usualCity.getId());
                            mUsualCityAdapter.setDataList(DataSupport.findAll(UsualCity.class));
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("算了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
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
            presenter.loadData(mWeatherId, mParentWeatherId);
        } else {
            WeatherDataParseBean bean = presenter.getDataByWeatherId(mWeatherId, MyConst.COMMON_WEATHER_URL);
            if (bean != null) {
                com.wjk.jweather.weather.bean.weatherbeen.Heweather6 weather
                        = JsonUtil.handleWeatherResponse(bean.getContentStr());
                showWeatherInfo(weather);
                bean = presenter.getDataByWeatherId(mParentWeatherId, MyConst.AIR_QUALITY_URL);
                if (bean != null) {
                    com.wjk.jweather.weather.bean.airbeen.Heweather6 aiq6 = JsonUtil.handleAiqResponse(bean.getContentStr());
                    showAqi(aiq6);
                }
                showNoNetTip();
            } else {
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
            mParentWeatherId = getIntent().getStringExtra("parent_id");
            presenter.loadData(mWeatherId, mParentWeatherId);
        }
        super.onNewIntent(intent);
    }

    private void setBg(String state) {
        int bgId = R.mipmap.default_bg;
        if (state.contains("晴")) {
            if (CommonUtil.isNigthNow()) {
                bgId = R.mipmap.sunny_night;
            } else {
                bgId = R.mipmap.sunny;
            }
        } else if (state.contains("多云")) {
            bgId = R.mipmap.cloudy_day;
        } else if (state.contains("雾")) {
            bgId = R.mipmap.frog;
        } else if (state.contains("雨")) {
            bgId = R.mipmap.rainy;
        } else if (state.contains("雪")) {
            if (CommonUtil.isNigthNow()) {
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
        String currAreaTitle = CommonUtil.makeAreaTile(weather.getBasic().getLocation(),
                weather.getBasic().getParentCity(),
                weather.getBasic().getAdminArea(), " ");
        setTitle(currAreaTitle);

        initWeatherHeader(weather);

        initHourlyForecast(weather);

        initLifeStyle(weather);

        initDailyForecast(weather);

        if (sr_pull_fresh.isRefreshing()) {
            sr_pull_fresh.setRefreshing(false);
            Toast.makeText(WeatherActivity.this,
                    "更新成功！", Toast.LENGTH_SHORT).show();

        }
        weatherLayout.setVisibility(View.VISIBLE);
        hideLoading();
    }

    private void initWeatherHeader(Heweather6 weather) {
        titleUpdateTime.setText("更新时间：" + weather.getUpdate().getLoc());
        degreeText.setText(weather.getNow().getTmp() + "℃");
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/mjnumber.ttf");
        // 应用字体
        degreeText.setTypeface(typeFace);
        weatherInfoText.setText(weather.getNow().getCondTxt());
        setBg(weather.getNow().getCondTxt());
        nowWind.setText(weather.getNow().getWindDir() + "-" + weather.getNow().getWindSc());
        String condCode = weather.getNow().getCondCode();
        if(CommonUtil.isNigthNow()){
            CommonUtil.showWeatherIcoNight(this,condCode,iv_weather_ico);
        }else{
            CommonUtil.showWeatherIcoDay(this,condCode,iv_weather_ico);
        }
    }

    private void initHourlyForecast(Heweather6 weather) {
        List<Hourly> hourly = weather.getHourly();
        if (hourly != null && hourly.size() > 0) {
            MutiItemsAdapter adapter = new MutiItemsAdapter(this);
            RecyclerView.LayoutManager layoutManager =
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            hourlyLayout.setLayoutManager(layoutManager);
            hourlyLayout.setAdapter(adapter);
            adapter.setDataList(hourly);
            cv_hourly_wrapper.setVisibility(View.VISIBLE);
        } else {
            cv_hourly_wrapper.setVisibility(View.GONE);
        }
    }

    private void initDailyForecast(Heweather6 weather) {
        List<DailyForecast> dailyForecasts = weather.getDailyForecast();
        if (dailyForecasts != null && dailyForecasts.size() > 0) {
            tv_forecast_title.setText(getString(R.string.forecast_title, weather.getDailyForecast().size()));
            MutiItemsAdapter adapter = new MutiItemsAdapter(this, MutiItemsAdapter.TYPE_DAILY);
            RecyclerView.LayoutManager layoutManager =
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            forecastLayout.setLayoutManager(layoutManager);
            forecastLayout.setAdapter(adapter);
            adapter.setDataList(dailyForecasts);
            ll_daily_forecast_wrapper.setVisibility(View.VISIBLE);
        } else {
            ll_daily_forecast_wrapper.setVisibility(View.GONE);
        }
    }

    private void initLifeStyle(Heweather6 weather) {
        for (int i = 0; i < weather.getLifestyle().size(); i++) {
            Lifestyle lifestyle = weather.getLifestyle().get(i);
            String type = lifestyle.getType();
            String typeName = LifestyleMap.styleVales.get(type);
            String text = typeName + "\n\n" + lifestyle.getBrf();
            //+ "\n" + weather.getLifestyle().get(i).getTxt();
            TextView itemView = (TextView) lifeStyleLayout.getChildAt(i);
            itemView.setText(text);
            itemView.setOnClickListener(new LifeStyleItemOnclickListener(lifestyle));
        }
    }

    private class LifeStyleItemOnclickListener implements View.OnClickListener {

        private final Lifestyle lifestyle;

        public LifeStyleItemOnclickListener(Lifestyle lifestyle) {
            this.lifestyle = lifestyle;
        }

        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(WeatherActivity.this)
                    .setTitle(lifestyle.getBrf())
                    .setMessage(lifestyle.getTxt())
                    .setPositiveButton("知道啦", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }


    @Override
    public void showGetWeatherFail(String msg) {
        sr_pull_fresh.setRefreshing(false);
        weatherLayout.setVisibility(View.GONE);
        if (msg.contains("解析")) {
            showNoData();
        } else {
            showNetError();
        }
    }

    @Override
    public void showAqi(com.wjk.jweather.weather.bean.airbeen.Heweather6 aiqObj) {
        AirNowCity airNow = aiqObj.getAirNowCity();
        airQuality.setText(airNow.getQlty());
        cp_api_panel.setProgress(Float.parseFloat(airNow.getAqi()));
        pm25Text.setProgress(Float.parseFloat(airNow.getPm25()));
        pm10.setProgress(Float.parseFloat(airNow.getPm10()));
        tv_air_co.setProgress(Float.parseFloat(airNow.getCo()));
        tv_so2.setProgress(Float.parseFloat(airNow.getSo2()));
        tv_o3.setProgress(Float.parseFloat(airNow.getO3()));
        tv_no2.setProgress(Float.parseFloat(airNow.getNo2()));
        weatherAiqLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAqiFail(String msg) {
        weatherAiqLayout.setVisibility(View.GONE);
        if (msg.contains("解析")) {
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
