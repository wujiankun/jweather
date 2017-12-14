package com.wjk.jweather.weather.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wjk.jweather.R;
import com.wjk.jweather.util.CommonUtil;
import com.wjk.jweather.util.MyConst;
import com.wjk.jweather.view.WithLineTempView;
import com.wjk.jweather.weather.bean.weatherbeen.DailyForecast;
import com.wjk.jweather.weather.bean.weatherbeen.Hourly;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wujiankun on 2017/10/17.
 * 天气列表的适配器
 */

public class MutiItemsAdapter extends RecyclerView.Adapter {
    private List<Object> items = new ArrayList<>();
    private Context mContext;
    private AssetManager assetManager;
    public static final int TYPE_HOURLY = 1;
    public static final int TYPE_DAILY = 2;
    private int mType = TYPE_HOURLY;
    private List<DailyForecast> dailyForecasts;

    public MutiItemsAdapter(Context ctx){
        mContext = ctx;
        assetManager = ctx.getResources().getAssets();
    }

    public MutiItemsAdapter(Context ctx,int listType){
        Log.i("wjk","view MutiItemsAdapter()");
        mContext = ctx;
        assetManager = ctx.getResources().getAssets();
        mType = listType;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mType==TYPE_HOURLY){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_hourly, parent, false);
            return new HourlyHolder(v);
        }else{
            Log.i("wjk","view onCreateViewHolder");
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_daily, parent, false);
            return new DailyHolder(v);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(mType==TYPE_HOURLY){
            HourlyHolder myHolder = (HourlyHolder) holder;
            Hourly hourly = (Hourly) items.get(position);
            String theTimeStr = hourly.getTime().split(" ")[1];
            myHolder.tv_time.setText(theTimeStr);
            myHolder.tv_weather.setText(hourly.getCondTxt());
            //myHolder.tv_temp.setText(hourly.getTmp() + "℃");
            setTempData(items,position,myHolder.vt_temp);
            myHolder.tv_wind.setText(hourly.getWindDir() + " " + hourly.getWindSc());
            String condCode = hourly.getCondCode();
            if(CommonUtil.isNigthNow(Integer.parseInt(theTimeStr.split(":")[0]))){
                CommonUtil.showWeatherIcoNight(mContext,condCode,myHolder.iv_weather_ico);
            }else {
                CommonUtil.showWeatherIcoDay(mContext,condCode,myHolder.iv_weather_ico);
            }
        }else{
            Log.i("wjk","view onBindViewHolder");
            DailyHolder dHolder = (DailyHolder) holder;
            DailyForecast forecast = (DailyForecast) items.get(position);
            Date theDate = forecast.getDate();
            dHolder.tv_weekday.setText(MyConst.weekTxt[theDate.getDay()]);
            String dateText = generateDateText(theDate);
            dHolder.tv_date.setText(dateText);
            dHolder.tv_weather_info_d.setText(forecast.getCondTxtD());
            CommonUtil.showWeatherIcoDay(mContext,forecast.getCondCodeD(),dHolder.iv_weather_ico_d);
           /* dHolder.tv_temp_max.setText(forecast.getTmpMax() + "℃");
            dHolder.tv_temp_min.setText(forecast.getTmpMin() + "℃");*/
            setTempData(items,position,dHolder.vt_temp);
            CommonUtil.showWeatherIcoNight(mContext,forecast.getCondCodeN(),dHolder.iv_weather_ico_n);
            dHolder.tv_weather_info_n.setText(forecast.getCondTxtN());
            if(forecast.getWindDir().equals("无持续风向")){
                dHolder.tv_wind_dir.setText("无风向");
            }else {
                dHolder.tv_wind_dir.setText(forecast.getWindDir());
            }
            dHolder.tv_wind_level.setText(forecast.getWindSc());
        }
    }

    private void setTempData(List<Object> items, int pos, WithLineTempView vt_temp){
        int maxTemp = Integer.MIN_VALUE,minTemp = Integer.MAX_VALUE;
        for (Object obj:items){
            if(obj instanceof  DailyForecast){
                DailyForecast temp = (DailyForecast)obj;
                int tepMax = Integer.parseInt(temp.getTmpMax());
                int tepMin = Integer.parseInt(temp.getTmpMin());
                if(maxTemp<tepMax){
                    maxTemp=tepMax;
                }
                if(minTemp>tepMin){
                    minTemp=tepMin;
                }
            }else if(obj instanceof Hourly){
                Hourly temp = (Hourly)obj;
                int tep = Integer.parseInt(temp.getTmp());
                if(maxTemp<tep){
                    maxTemp=tep;
                }
                if(minTemp>tep){
                    minTemp=tep;
                }
            }
        }
        int [] tempLast = null;
        int [] tempNext = null;
        int [] tempNow = null;

        if(items.get(pos) instanceof  DailyForecast){
            tempNow = new int[4];
            DailyForecast temp = (DailyForecast) items.get(pos);
            tempNow[0] = Integer.parseInt(temp.getTmpMax());
            tempNow[1] = Integer.parseInt(temp.getTmpMin());
            tempNow[2] = minTemp;tempNow[3]=maxTemp;
            if(pos-1>=0){
                temp = (DailyForecast) items.get(pos-1);
                tempLast = new int[2];
                tempLast[0]= Integer.parseInt(temp.getTmpMax());
                tempLast[1]= Integer.parseInt(temp.getTmpMin());
            }
            if(pos+1<=items.size()-1){
                temp = (DailyForecast) items.get(pos+1);
                tempNext = new int[2];
                tempNext[0]= Integer.parseInt(temp.getTmpMax());
                tempNext[1]= Integer.parseInt(temp.getTmpMin());
            }
        }else if(items.get(pos) instanceof Hourly){
            tempNow = new int[3];
            Hourly temp = (Hourly) items.get(pos);
            tempNow[0] = Integer.parseInt(temp.getTmp());
            tempNow[1] = minTemp;tempNow[2]=maxTemp;
            if(pos-1>=0){
                temp = (Hourly) items.get(pos-1);
                tempLast = new int[1];
                tempLast[0]= Integer.parseInt(temp.getTmp());
            }
            if(pos+1<=items.size()-1){
                temp = (Hourly) items.get(pos+1);
                tempNext = new int[1];
                tempNext[0]= Integer.parseInt(temp.getTmp());
            }
        }



        vt_temp.setData(tempNow,tempLast,tempNext);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    static class HourlyHolder extends RecyclerView.ViewHolder{
        TextView tv_weather;
        TextView tv_time;
        //TextView tv_temp;
        WithLineTempView vt_temp;
        TextView tv_wind;
        ImageView iv_weather_ico;
        HourlyHolder(View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_weather = itemView.findViewById(R.id.tv_weather);
            //tv_temp = itemView.findViewById(R.id.tv_temp);
            vt_temp = itemView.findViewById(R.id.vt_temp);
            tv_wind = itemView.findViewById(R.id.tv_wind);
            iv_weather_ico = itemView.findViewById(R.id.iv_weather_ico);
        }
    }

    static class DailyHolder extends RecyclerView.ViewHolder{
        TextView tv_weekday;
        TextView tv_date;
        TextView tv_weather_info_d;
        ImageView iv_weather_ico_d;
       /* TextView tv_temp_max;
        TextView tv_temp_min;*/
       WithLineTempView vt_temp;
        ImageView iv_weather_ico_n;
        TextView tv_weather_info_n;
        TextView tv_wind_dir;
        TextView tv_wind_level;
        DailyHolder(View itemView) {
            super(itemView);
            tv_weekday = itemView.findViewById(R.id.tv_weekday);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_weather_info_d = itemView.findViewById(R.id.tv_weather_info_d);
            iv_weather_ico_d = itemView.findViewById(R.id.iv_weather_ico_d);
            /*tv_temp_max = itemView.findViewById(R.id.tv_temp_max);
            tv_temp_min = itemView.findViewById(R.id.tv_temp_min);*/
            vt_temp = itemView.findViewById(R.id.vt_temp);
            iv_weather_ico_n = itemView.findViewById(R.id.iv_weather_ico_n);
            tv_weather_info_n = itemView.findViewById(R.id.tv_weather_info_n);
            tv_wind_dir = itemView.findViewById(R.id.tv_wind_dir);
            tv_wind_level = itemView.findViewById(R.id.tv_wind_level);
        }
    }

    public void setDataList(List datas){
        items.clear();
        items.addAll(datas);
        notifyDataSetChanged();
    }

    private String generateDateText(Date theDate) {
        int month = theDate.getMonth();
        int date = theDate.getDate();
        Date now = new Date();
        int nowMonth = now.getMonth();
        int nowDate = now.getDate();
        String dateTxt;
        if (month == nowMonth && date == nowDate) {
            dateTxt = "今天";
        } else if (month == nowMonth && date == nowDate + 1) {
            dateTxt = "明天";
        } else {
            dateTxt = (month + 1) + "-" + (date > 9 ? date : ("0" + date));
        }
        return dateTxt;
    }
}
