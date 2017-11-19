package com.wjk.jweather.weather.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wjk.jweather.R;
import com.wjk.jweather.db.UsualCity;
import com.wjk.jweather.weather.bean.weatherbeen.Hourly;
import com.wjk.jweather.weather.ui.WeatherActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiankun on 2017/10/17.
 * 常用地区列表的适配器
 */

public class MutiItemsAdapter extends RecyclerView.Adapter {
    private List<Hourly> items = new ArrayList<>();
    private Context mContext;
    private AssetManager assetManager;
    public MutiItemsAdapter(Context ctx){
        mContext = ctx;
        assetManager = ctx.getResources().getAssets();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly, parent, false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        Hourly hourly = items.get(position);
        myHolder.tv_time.setText(hourly.getTime().split(" ")[1]);
        myHolder.tv_weather.setText(hourly.getCondTxt());
        myHolder.tv_temp.setText(hourly.getTmp() + "℃");
        myHolder.tv_wind.setText(hourly.getWindDir() + "-" + hourly.getWindSc());
        String condCode = hourly.getCondCode();
        Glide.with(mContext).load("file:///android_asset/ico/"+condCode+".png").into(myHolder.iv_weather_ico);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_weather;
        TextView tv_time;
        TextView tv_temp;
        TextView tv_wind;
        ImageView iv_weather_ico;
        MyHolder(View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_weather = itemView.findViewById(R.id.tv_weather);
            tv_temp = itemView.findViewById(R.id.tv_temp);
            tv_wind = itemView.findViewById(R.id.tv_wind);
            iv_weather_ico = itemView.findViewById(R.id.iv_weather_ico);
        }
    }

    public void setDataList(List<Hourly> datas){
        items.clear();
        items.addAll(datas);
        notifyDataSetChanged();
    }
}
