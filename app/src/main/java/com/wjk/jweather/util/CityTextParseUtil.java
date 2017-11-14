package com.wjk.jweather.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.wjk.jweather.db.AreaParseBean;
import com.wjk.jweather.db.CityParseBean;
import com.wjk.jweather.db.ProvinceParseBean;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiankun on 2017/11/14.
 * 读取城市列表文件的每一行，并作为一个条目保存到数据库，过程中保存省的信息。
 */

public class CityTextParseUtil {

    public void readTextAndSaveToDb(Context context) {
        AssetManager am=context.getAssets();
        AreaParseBean lastBean = null;
        try {
            InputStream is=am.open("city-list.txt");
            String code=getCode(is);
            is=am.open("city-list.txt");
            BufferedReader br=new BufferedReader(new InputStreamReader(is,code));
            String line;
            int i=0;
            while((line=br.readLine())!=null){
                String[] s = line.split("\t");
                AreaParseBean bean = new AreaParseBean();
                bean.setAreaCode(s[0]);
                bean.setAreaEN(s[1]);
                bean.setAreaCN(s[2]);
                bean.setCountryCode(s[3]);
                bean.setCountryEN(s[4]);
                bean.setCountryCN(s[5]);
                bean.setProvinceEN(s[6]);
                bean.setProvinceCN(s[7]);
                bean.setParentAreaEN(s[8]);
                bean.setParentAreaCN(s[9]);
                bean.setLatitude(Double.parseDouble(s[10]));
                bean.setLongitude(Double.parseDouble(s[11]));
                bean.save();
                checkAndSaveNewProvice(bean,lastBean,i);
                lastBean = bean;
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ProvinceParseBean tempProvince;

    private void checkAndSaveNewProvice(AreaParseBean newer,AreaParseBean old,int i){
        //CN101010100
        int diffStart = 0,diffEnd = 7;
        if(old==null){
            tempProvince = makeNewProvince(newer);
            return;
        }
        String newCode = newer.getAreaCode();
        String oldCode = old.getAreaCode();
        String substringOldCode;
        if(i==1){
            substringOldCode = oldCode.substring(diffStart+1, diffEnd+1);
        }else{
            substringOldCode = oldCode.substring(diffStart, diffEnd);
        }
        String substringNewCode = newCode.substring(diffStart, diffEnd);
       /* int noIndex = oldCode.indexOf("N");
        int nnIndex  = newCode.indexOf("N");
        String substringNewCode = newCode.substring(nnIndex+1, nnIndex+6);
        String substringOldCode = oldCode.substring(noIndex+1, noIndex+6);*/
        if(substringNewCode.equals(substringOldCode)){
            //与上一条数据属于同一个省
            List<CityParseBean> cities = tempProvince.getCities();
            if(cities.size()<1){
                //当前省份还没有市级数据，把newer转换之
                cities.add(makeNewCity(newer));
                tempProvince.setCities(cities);
            }else{
                //当前省份已经有保存的市级数据，取出最后一个，再比较是不是同一个市的
                CityParseBean cityParseBean = cities.get(cities.size() - 1);
                String areaCityCode = cityParseBean.getAreaCode();
                if(i==1){
                    substringOldCode = areaCityCode.substring(diffStart+1, diffEnd+1+2);
                }else{
                    substringOldCode = areaCityCode.substring(diffStart, diffEnd+2);
                }
                substringNewCode = newCode.substring(diffStart, diffEnd+2);
                if(substringNewCode.equals(substringOldCode)){
                    //与上一条数据属于同一个省，并且是同一个城市
                    //再比较是不是同一个区
                    List<AreaParseBean> block = cityParseBean.getBlocks();
                    if(block.size()<1){
                        //当前city还没有block数据，把newer转换之
                        block.add(newer);
                    }else{
                        //当前city已经有保存的block数据，取出最后一个，再比较是不是同一个block的
                        AreaParseBean blockBean = block.get(block.size() - 1);
                        String blockCode = blockBean.getAreaCode();
                        if(i==1){
                            substringOldCode = blockCode.substring(diffStart+1, diffEnd+1+4);
                        }else{
                            substringOldCode = blockCode.substring(diffStart, diffEnd+4);
                        }
                        substringNewCode = newCode.substring(diffStart, diffEnd+4);
                        if(substringNewCode.equals(substringOldCode)){
                            //与上一条数据属于同一个省，并且是同一个城市，并且还是一个区

                        }else{
                            //与上一条数据属于同一个省，并且是同一个城市，但不是一个区
                            block.add(newer);
                        }
                    }
                    cityParseBean.setBlocks(block);

                }else{
                    //与上一条数据属于同一个省，但不是同一个城市
                    cities.add(makeNewCity(newer));
                }
           }
            return;
        }
        tempProvince.save();
        tempProvince = makeNewProvince(newer);
    }

    private CityParseBean makeNewCity(AreaParseBean n) {
        CityParseBean c = new CityParseBean();
        c.setAreaCode(n.getAreaCode());
        c.setCountryCN(n.getCountryCN());
        c.setCountryCode(n.getCountryCode());
        c.setCountryEN(n.getCountryEN());
        c.setProvinceCN(n.getProvinceCN());
        c.setProvinceEN(n.getProvinceEN());
        c.setBlocks(new ArrayList<AreaParseBean>());
        return c;
    }

    private ProvinceParseBean makeNewProvince(AreaParseBean n) {
        ProvinceParseBean p = new ProvinceParseBean();
        p.setAreaCode(n.getAreaCode());
        p.setCountryCN(n.getCountryCN());
        p.setCountryCode(n.getCountryCode());
        p.setCountryEN(n.getCountryEN());
        p.setProvinceCN(n.getProvinceCN());
        p.setProvinceEN(n.getProvinceEN());
        p.setCities(new ArrayList<CityParseBean>());
        return p;
    }

    /*private static void checkAndSaveNewProvice(AreaParseBean newer,AreaParseBean old,int i){
        //CN101010100
        int diffStart = 0,diffEnd = 7;

        if(old==null){
            makeNewProvince(newer);
            return;
        }
        String newCode = newer.getAreaCode();
        String oldCode = old.getAreaCode();
        String substringOldCode;
        if(i==1){
            substringOldCode = oldCode.substring(diffStart+1, diffEnd+1);
        }else{
            substringOldCode = oldCode.substring(diffStart, diffEnd);
        }
        String substringNewCode = newCode.substring(diffStart, diffEnd);
       *//* int noIndex = oldCode.indexOf("N");
        int nnIndex  = newCode.indexOf("N");
        String substringNewCode = newCode.substring(nnIndex+1, nnIndex+6);
        String substringOldCode = oldCode.substring(noIndex+1, noIndex+6);*//*
        if(substringNewCode.equals(substringOldCode)){
            return;
        }
        makeNewProvince(newer);
    }*/

    /*private void makeNewProvince(AreaParseBean n) {
        ProvinceParseBean p = new ProvinceParseBean();
        p.setAreaCode(n.getAreaCode());
        p.setCountryCN(n.getCountryCN());
        p.setCountryCode(n.getCountryCode());
        p.setCountryEN(n.getCountryEN());
        p.setProvinceCN(n.getProvinceCN());
        p.setProvinceEN(n.getProvinceEN());
        p.save();
        Log.e("wjk","save bean:"+p.getProvinceCN());
    }*/

    private String getCode(InputStream is){
        try {
            BufferedInputStream bin = new BufferedInputStream(is);
            int p = (bin.read() << 8) + bin.read();
            String code;
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
            is.close();
            return code;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
