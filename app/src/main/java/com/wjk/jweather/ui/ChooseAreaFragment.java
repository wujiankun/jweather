package com.wjk.jweather.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wjk.jweather.R;
import com.wjk.jweather.db.AreaParseBean;
import com.wjk.jweather.db.City;
import com.wjk.jweather.db.CityParseBean;
import com.wjk.jweather.db.County;
import com.wjk.jweather.db.ProvinceParseBean;
import com.wjk.jweather.db.UsualCity;
import com.wjk.jweather.util.CityTextParseUtil;
import com.wjk.jweather.util.GsonUtil;
import com.wjk.jweather.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseAreaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseAreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseAreaFragment extends BaseFragment {
    private static final int level_province = 1;
    private static final int level_city = 2;
    private static final int level_county = 3;

    private ProgressDialog mProgressDialog;
    private TextView mTitleTextView;
    private Button mBackBtn;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> dataList = new ArrayList<>();
    private List<ProvinceParseBean> provinces;
    private List<CityParseBean> cities;
    private List<AreaParseBean> blocks;

    private ProvinceParseBean mSelectProvince;
    private CityParseBean mSelectCity;

    private int mCurrentLevel;

    public ChooseAreaFragment() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    queryProvinces();
                    hideProgressDialog();
                    break;
                case 2:
                    if(mProgressDialog!=null&&mProgressDialog.isShowing()){
                        mProgressDialog.setMessage("解析城市数据中..."+msg.arg1+"/"+msg.arg2);
                    }
                    break;
            }
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseAreaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseAreaFragment newInstance(String param1, String param2) {
        ChooseAreaFragment fragment = new ChooseAreaFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
        int addCityKey = getActivity().getIntent().getIntExtra("add_city",-1);
        if(addCityKey<0){
            List<UsualCity> usualCities = DataSupport.findAll(UsualCity.class);
            if(usualCities!=null&&usualCities.size()>0){
                UsualCity loveCity = usualCities.get(0);
                for(UsualCity city:usualCities){
                    if(city.isLoveCity()>0){
                        loveCity = city;
                    }
                }
                goWeatherActivity(loveCity.getWeatherId());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_choose_area,container);
        mTitleTextView = layout.findViewById(R.id.title_text);
        mBackBtn = layout.findViewById(R.id.back_btn);
        mListView = layout.findViewById(R.id.list_view);
        mAdapter = new ArrayAdapter<>(getContext(),R.layout.item_area,dataList);
        mListView.setAdapter(mAdapter);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置listview与button的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (mCurrentLevel){
                    case level_province:
                        mSelectProvince = provinces.get(position);
                        queryCities();
                        break;
                    case level_city:
                        mSelectCity = cities.get(position);
                        int size = queryBlocks();
                        if(size<1){//如果是直辖市，直接拿weatherId 转向天气界面
                            String weatherId = mSelectCity.getAreaCode();
                            saveArea(mSelectCity);
                            mCurrentLevel = level_county;
                            goWeatherActivity(weatherId);
                        }
                        break;
                    case level_county:
                        String weatherId = blocks.get(position).getAreaCode();
                        saveArea(blocks.get(position));
                        goWeatherActivity(weatherId);
                        break;
                }
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mCurrentLevel){
                    case level_county:
                        queryCities();
                        break;
                    case level_city:
                        queryProvinces();
                        break;
                }
            }
        });
        queryProvinces();
    }

    private void goWeatherActivity(String weatherId) {
        Intent intent = new Intent(getActivity(),WeatherActivity.class);
        intent.putExtra("weather_id",weatherId);
        intent.putExtra("from_choose_area",true);
        startActivity(intent);
        getActivity().finish();
    }

    private void saveArea(AreaParseBean county) {
        ContentValues values = new ContentValues();
        values.put("isLoveCity",0);
        DataSupport.updateAll(UsualCity.class,values,"isLoveCity=?","1");
        UsualCity city = new UsualCity();
        city.setCountyName(county.getAreaCN());
        city.setWeatherId(county.getAreaCode());
        city.setLoveCity(1);
        city.setProvinceName(county.getProvinceCN());
        city.save();
    }

    private void saveArea(CityParseBean mSelectCity) {
        AreaParseBean bean = new AreaParseBean(mSelectCity);
        saveArea(bean);
    }

    private void queryProvinces() {
        mTitleTextView.setText("国内");
        mBackBtn.setVisibility(View.GONE);
        provinces = DataSupport.findAll(ProvinceParseBean.class);
        if(provinces.size()>0){
            dataList.clear();
            for(ProvinceParseBean p:provinces){
                dataList.add(p.getProvinceCN());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = level_province;
        }else{
            showProgressDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new CityTextParseUtil().readTextAndSaveToDb(getContext(),mHandler);
                    mHandler.sendEmptyMessage(1);
                }
            }).start();
        }
    }

    private int queryBlocks() {
        mTitleTextView.setText(mSelectCity.getParentAreaCN());
        mBackBtn.setVisibility(View.VISIBLE);
        blocks = DataSupport.where("cityparsebean_id = ?",
                String.valueOf(mSelectCity.getId())).find(AreaParseBean.class);
        if(blocks.size()>0){
            dataList.clear();
            for(AreaParseBean c: blocks){
                dataList.add(c.getAreaCN());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = level_county;
        }
        return blocks.size();
    }

    private void queryCities() {
        mTitleTextView.setText(mSelectProvince.getProvinceCN());
        mBackBtn.setVisibility(View.VISIBLE);
        cities = DataSupport.where("provinceparsebean_id = ?",
                String.valueOf(mSelectProvince.getId())).find(CityParseBean.class);
        if(cities.size()>0){
            dataList.clear();
            for(CityParseBean c:cities){
                dataList.add(c.getAreaCN());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = level_city;
        }else{
           /* int provinceCode = mSelectProvince.getCode();
            String url = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(url,"city");*/
        }
    }


    private void showProgressDialog() {
        if(mProgressDialog==null){
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("解析城市数据中...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog(){
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
            mProgressDialog=null;
        }
        if(mHandler!=null){
            mHandler.removeCallbacks(null);
            mHandler = null;
        }
    }


    @Override
    public boolean handleBackPress() {
        switch (mCurrentLevel){
            case level_county:
                queryCities();
                return true;
            case level_city:
                queryProvinces();
                return true;
        }
        return false;
    }
}
