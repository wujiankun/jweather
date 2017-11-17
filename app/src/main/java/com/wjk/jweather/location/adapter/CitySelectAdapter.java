package com.wjk.jweather.location.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wjk.jweather.R;
import com.wjk.jweather.db.BaseAreaParseBean;
import com.wjk.jweather.listener.CityChangeListener;

import java.util.List;

/**
 * Created by wujiankun on 2017/10/17.
 * 常用地区列表的适配器
 */

public class CitySelectAdapter extends RecyclerView.Adapter {
    private List<BaseAreaParseBean> cities;
    private CityChangeListener listener;
    private boolean isProviceList = false;
    public CitySelectAdapter(List<BaseAreaParseBean> cities, CityChangeListener listener){
        this.cities = cities;
        this.listener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_area, parent, false);
        final MyHolder holder = new MyHolder(v);
        holder.cityNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                listener.onCityChange(cities.get(position),position);
            }
        });
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        BaseAreaParseBean city = cities.get(position);
        String itemText=isProviceList?city.getProvinceCN():city.getAreaCN();
        myHolder.cityNameView.setText(itemText);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView cityNameView;
        MyHolder(ViewGroup itemView) {
            super(itemView);
            cityNameView = itemView.findViewById(R.id.tv_area_name);
        }
    }

    public void setIsProvince(boolean isProvinceList){
        this.isProviceList = isProvinceList;
        notifyDataSetChanged();
    }
}
