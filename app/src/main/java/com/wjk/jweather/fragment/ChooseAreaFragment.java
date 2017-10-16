package com.wjk.jweather.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.wjk.jweather.db.City;
import com.wjk.jweather.db.County;
import com.wjk.jweather.db.Province;
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
public class ChooseAreaFragment extends Fragment {
    private static final int level_province = 1;
    private static final int level_city = 2;
    private static final int level_county = 3;

    private ProgressDialog mProgressDialog;
    private TextView mTitleTextView;
    private Button mBackBtn;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinces;
    private List<City> cities;
    private List<County> counties;

    private Province mSelectProvince;
    private City mSelectCity;
    private County mSelectCounty;

    private int mCurrentLevel;

    private OnFragmentInteractionListener mListener;

    public ChooseAreaFragment() {
        // Required empty public constructor
    }

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
                        queryCounties();
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

    private void queryProvinces() {
        mTitleTextView.setText("国内");
        mBackBtn.setVisibility(View.GONE);
        provinces = DataSupport.findAll(Province.class);
        if(provinces.size()>0){
            dataList.clear();
            for(Province p:provinces){
                dataList.add(p.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = level_province;
        }else{
            String url = "http://guolin.tech/api/china";
            queryFromServer(url,"province");
        }
    }


    private void queryCounties() {
        mTitleTextView.setText(mSelectCity.getCityName());
        mBackBtn.setVisibility(View.VISIBLE);
        counties = DataSupport.where("cityId = ?",String.valueOf(mSelectCity.getCode())).find(County.class);
        if(counties.size()>0){
            dataList.clear();
            for(County c:counties){
                dataList.add(c.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = level_county;
        }else{
            int provinceCode = mSelectProvince.getCode();
            int cityCode = mSelectCity.getCode();
            String url = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(url,"county");
        }
    }

    private void queryCities() {
        mTitleTextView.setText(mSelectProvince.getProvinceName());
        mBackBtn.setVisibility(View.VISIBLE);
        cities = DataSupport.where("provinceId = ?",String.valueOf(mSelectProvince.getId())).find(City.class);
        if(cities.size()>0){
            dataList.clear();
            for(City c:cities){
                dataList.add(c.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = level_city;
        }else{
            int provinceCode = mSelectProvince.getCode();
            String url = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(url,"city");
        }
    }

    private void queryFromServer(String url, final String type) {
        showProgressDialog();
        HttpUtil.sendRequest(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        Toast.makeText(getContext(),"load failed...",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                boolean result = false;
                String responseText = response.body().string();
                if(type.equals("province")){
                    result = GsonUtil.handleProvinceResponse(responseText);
                }else if(type.equals("city")){
                    result = GsonUtil.handleCityResponse(responseText,mSelectProvince.getCode());
                }else if(type.equals("county")){
                    result = GsonUtil.handleCountyResponse(responseText,mSelectCity.getCode());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                            if(type.equals("province")){
                                queryProvinces();
                            }else if(type.equals("city")){
                                queryCities();
                            }else if(type.equals("county")){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog() {
        if(mProgressDialog==null){
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Loading...");
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
            mProgressDialog=null;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
