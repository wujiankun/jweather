package com.wjk.jweather.weather.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wjk.jweather.R;
import com.wjk.jweather.db.UsualCity;
import com.wjk.jweather.listener.CityChangeListener;
import com.wjk.jweather.util.CommonUtil;

import java.util.List;

/**
 * Created by wujiankun on 2017/10/17.
 * 常用地区列表的适配器
 */

public class UsualCityAdapter extends RecyclerView.Adapter {
    private List<UsualCity> cities;
    private CityChangeListener listener;
    public UsualCityAdapter(List<UsualCity> cities,CityChangeListener listener){
        this.cities = cities;
        this.listener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_area, parent, false);
        final MyHolder holder = new MyHolder(v);

        holder.cityNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                listener.onCityChange(cities.get(position), position);
            }
        });
        holder.cityNameView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = holder.getAdapterPosition();
                listener.onCityDelete(cities.get(position), position);
                return true;
            }
        });
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        UsualCity city = cities.get(position);
        String cityName = city.getParentAreaCN();
        String blockName = city.getAreaCN();
        String currAreaTitle = CommonUtil.makeAreaTile(blockName,
                cityName,
                city.getProvinceCN(),"·");
        myHolder.cityNameView.setText(currAreaTitle);
        myHolder.cityNameView.setSelected(city.isLoveCity()==1);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView cityNameView;
        MyHolder(View itemView) {
            super(itemView);
            cityNameView = itemView.findViewById(R.id.tv_area_name);
        }
    }

    public void setDataList(List<UsualCity> cities){
        this.cities = cities;
        notifyDataSetChanged();
    }
}
