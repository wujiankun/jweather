package com.wjk.jweather.location;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wjk.jweather.R;
import com.wjk.jweather.location.adapter.CitySelectAdapter;
import com.wjk.jweather.base.BaseFragment;
import com.wjk.jweather.db.AreaParseBean;
import com.wjk.jweather.db.BaseAreaParseBean;
import com.wjk.jweather.db.CityParseBean;
import com.wjk.jweather.db.ProvinceParseBean;
import com.wjk.jweather.db.UsualCity;
import com.wjk.jweather.listener.CityChangeListener;
import com.wjk.jweather.util.CityTextParseUtil;
import com.wjk.jweather.weather.ui.WeatherActivity;

import org.litepal.crud.DataSupport;

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
    private RecyclerView mListView;
    private CitySelectAdapter mAdapter;
    private List<BaseAreaParseBean> dataList = new ArrayList<>();
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
                        mProgressDialog.setMessage("为您准备城市数据:"+msg.arg1+"/"+msg.arg2);
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
                goWeatherActivity(loveCity);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_choose_area;
    }

    @Override
    protected void initViews() {
        mTitleTextView = mRootView.findViewById(R.id.title_text);
        mBackBtn = mRootView.findViewById(R.id.back_btn);
        mListView = mRootView.findViewById(R.id.list_view);
        mAdapter = new CitySelectAdapter(dataList, new CityChangeListener() {
            @Override
            public void onCityChange(BaseAreaParseBean city, int position) {
                switch (mCurrentLevel){
                    case level_province:
                        mSelectProvince = provinces.get(position);
                        queryCities();
                        break;
                    case level_city:
                        mSelectCity = cities.get(position);
                        int size = queryBlocks();
                        if(size<1){//如果是直辖市，直接拿weatherId 转向天气界面
                            saveArea(mSelectCity);
                            mCurrentLevel = level_county;
                            goWeatherActivity(mSelectCity);
                        }
                        break;
                    case level_county:
                        saveArea(blocks.get(position));
                        goWeatherActivity(blocks.get(position));
                        break;
                }
            }
        });
        mListView.setAdapter(mAdapter);

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
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),3);
        mListView.setLayoutManager(manager);
        mListView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initData() {
        queryProvinces();
    }


    private void goWeatherActivity(BaseAreaParseBean city) {
        Intent intent = new Intent(getActivity(),WeatherActivity.class);
        intent.putExtra("weather_id",city.getAreaEN());
        intent.putExtra("from_choose_area",true);
        startActivity(intent);
        getActivity().finish();
    }

    private void saveArea(BaseAreaParseBean county) {
        //先查询常用目标地址是否已加入收藏
        List<UsualCity> usualCities = DataSupport.where("areacode=?", county.getAreaCode())
                .find(UsualCity.class);
        if(usualCities.size()>0){
            return;
        }
        ContentValues values = new ContentValues();
        values.put("isLoveCity",0);
        //查询出默认城市的id然后修改为非默认
        usualCities = DataSupport.where("isLoveCity=?", "1")
                .find(UsualCity.class);
        if(usualCities.size()>0){
            DataSupport.update(UsualCity.class,values,usualCities.get(0).getId());
        }
        UsualCity city = new UsualCity();
        city.copyValueFrom(county);
        city.setLoveCity(1);
        city.save();
    }
    private void queryProvinces() {
        mBackBtn.setVisibility(View.GONE);
        provinces = DataSupport.findAll(ProvinceParseBean.class);
        if(provinces.size()>0){
            dataList.clear();
            dataList.addAll(provinces);
            mAdapter.setIsProvince(true);
            mCurrentLevel = level_province;
            mTitleTextView.setText("请选择地区");
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
            dataList.addAll(blocks);
            mAdapter.setIsProvince(false);
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
            dataList.addAll(cities);
            mAdapter.setIsProvince(false);
            mCurrentLevel = level_city;
        }
    }


    private void showProgressDialog() {
        if(mProgressDialog==null){
            mProgressDialog = new ProgressDialog(getContext());
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

    public interface OnFragmentInteractionListener {
        void onFragmentSelect(BaseFragment fragment);
    }
}
