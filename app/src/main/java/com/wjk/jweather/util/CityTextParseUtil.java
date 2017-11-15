package com.wjk.jweather.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wjk.jweather.db.AreaParseBean;
import com.wjk.jweather.db.CityParseBean;
import com.wjk.jweather.db.ProvinceParseBean;

import org.litepal.crud.DataSupport;

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

    public void readTextAndSaveToDb(Context context, Handler mHandler) {
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open("city-list.txt");
            String code = getCode(is);
            is = am.open("city-list.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, code));
            String line;
            String firstLine =  br.readLine();
            //剪掉txt文档的第一个无用字符
            firstLine = firstLine.substring(1,firstLine.length());
            parseTextByLine(firstLine,mHandler);
            while ((line = br.readLine()) != null) {
                parseTextByLine(line,mHandler);
            }
            saveProvinceToDb(tempProvince);
            tempProvince=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int i=1;
    private AreaParseBean tempBean = null;
    private void parseTextByLine(String line,Handler mHandler) {
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
        checkAndSaveNewProvince(bean, tempBean);
        tempBean = bean;
        i++;
        Message msg = mHandler.obtainMessage(2);
        msg.arg1 = i;msg.arg2 = 3181;
        mHandler.sendMessage(msg);
    }

    private ProvinceParseBean tempProvince;
    private void checkAndSaveNewProvince(AreaParseBean newer, AreaParseBean old) {
        if (old == null) {
            tempProvince = makeNewProvince(newer);
            List<CityParseBean> tempCities = tempProvince.getCities();
            CityParseBean tempCity = makeNewCity(newer);
            List<AreaParseBean> tempBlocks = tempCity.getBlocks();
            if(!isOnlyCity(newer)){
                tempBlocks.add(newer);
            }
            tempCity.setBlocks(tempBlocks);
            tempCities.add(tempCity);
            tempProvince.setCities(tempCities);
            return;
        }
        if (isSameProvince(newer,old)) {//与上一条数据属于同一个省
            //再比较是不是同一个市的
            if (isSameCity(newer,old)) {//与上一条数据属于同一个省，并且是同一个城市
                List<CityParseBean> tempCities = tempProvince.getCities();
                CityParseBean tempCity = tempCities.get(tempCities.size() - 1);
                List<AreaParseBean> tempBlocks = tempCity.getBlocks();
                if(!isOnlyCity(newer)){//当前地区不属于直辖市，保存当前地区到block
                    tempBlocks.add(newer);
                }
                tempCity.setBlocks(tempBlocks);
                tempCities.remove(tempCities.size() - 1);
                tempCities.add(tempCity);
                tempProvince.setCities(tempCities);

            } else {
                //与上一条数据属于同一个省，但不是同一个城市
                List<CityParseBean> tempCities = tempProvince.getCities();
                CityParseBean tempCity = makeNewCity(newer);
                List<AreaParseBean> tempBlocks = tempCity.getBlocks();
                if(!isOnlyCity(newer)){
                    tempBlocks.add(newer);
                }
                tempCity.setBlocks(tempBlocks);
                tempCities.add(makeNewCity(newer));
                tempProvince.setCities(tempCities);
            }
        }else{
            //与上一条数据属于不同的省，保存上一条数据到数据库，并生成新的省数据
            saveProvinceToDb(tempProvince);
            tempProvince = makeNewProvince(newer);
        }
    }



    //CN10101 01 00
    private final int diffStart = 0, diffEnd = 7;
    private boolean isSameProvince(AreaParseBean newer, AreaParseBean old){
        String newCode = newer.getAreaCode();
        String oldCode = old.getAreaCode();
        String substringOldCode= oldCode.substring(diffStart, diffEnd);
        String substringNewCode = newCode.substring(diffStart, diffEnd);
        return substringNewCode.equals(substringOldCode);
    }
    private boolean isSameCity(AreaParseBean newer, AreaParseBean old){
        String newCode = newer.getAreaCode();
        String oldCode = old.getAreaCode();
        String substringOldCode = oldCode.substring(diffStart, diffEnd + 2);
        String substringNewCode = newCode.substring(diffStart, diffEnd + 2);
        return substringNewCode.equals(substringOldCode);
    }
    //判断当前地区是否属于直辖市
    private boolean isOnlyCity(AreaParseBean cityParseBean) {
        String newCode = cityParseBean.getAreaCode();
        String lastTowCode= newCode.substring(diffEnd+2, newCode.length());
        return lastTowCode.equals("00");
    }

    private void saveProvinceToDb(ProvinceParseBean bean){
        List<CityParseBean> cities = bean.getCities();
        for (CityParseBean c:cities){
            List<AreaParseBean> blocks = c.getBlocks();
            for(AreaParseBean a:blocks){
                a.save();
                Log.e("wjk","save block:"+a.getProvinceCN()+" "+a.getParentAreaCN()+" "+a.getAreaCN());
            }
            c.save();
            Log.e("wjk","save city:"+c.getProvinceCN()+" "+" "+c.getParentAreaCN());
        }
        bean.save();
        Log.e("wjk","save province:"+bean.getProvinceCN()+" ");
    }

    private CityParseBean makeNewCity(AreaParseBean n) {
        CityParseBean c = new CityParseBean();
        c.setAreaCN(n.getAreaCN());
        c.setAreaEN(n.getAreaEN());
        c.setAreaCode(n.getAreaCode());
        c.setCountryCN(n.getCountryCN());
        c.setCountryCode(n.getCountryCode());
        c.setCountryEN(n.getCountryEN());
        c.setProvinceCN(n.getProvinceCN());
        c.setProvinceEN(n.getProvinceEN());
        c.setParentAreaCN(n.getParentAreaCN());
        c.setParentAreaEN(n.getParentAreaEN());
        c.setLatitude(n.getLatitude());
        c.setLongitude(n.getLongitude());
        /*if(!isOnlyCity(n)){
            List<AreaParseBean> blocks = c.getBlocks();
            blocks.add(n);
            c.setBlocks(blocks);
        }*/
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
      /*  List<CityParseBean> cities = p.getCities();
        cities.add(makeNewCity(n));
        p.setCities(cities);*/
        return p;
    }
    private String getCode(InputStream is) {
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
