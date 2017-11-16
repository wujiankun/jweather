package com.wjk.jweather.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wjk.jweather.db.AreaParseBean;
import com.wjk.jweather.db.CityParseBean;
import com.wjk.jweather.db.ProvinceParseBean;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by wujiankun on 2017/11/14.
 * 读取城市列表文件的每一行，并作为一个条目保存到数据库，过程中保存省的信息。
 */

public class CityTextParseUtil {
    private Handler mHandler;
    public void readTextAndSaveToDb(Context context, Handler mHandler) {
        this.mHandler = mHandler;
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
            parseTextByLine(firstLine);
            while ((line = br.readLine()) != null) {
                parseTextByLine(line);
            }
            saveProvinceToDb(tempProvince);
            tempProvince=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private AreaParseBean tempBean = null;
    private void parseTextByLine(String line) {
        String[] s = line.split("\t");
        AreaParseBean bean = new AreaParseBean();
        bean.initFromStrArr(s);
        checkAndSaveNewProvince(bean, tempBean);
        tempBean = bean;
    }

    private ProvinceParseBean tempProvince;
    private void checkAndSaveNewProvince(AreaParseBean newer, AreaParseBean old) {
        if (old == null) {
            tempProvince = makeNewProvince(newer);
            return;
        }
        if (isSameProvince(newer,old)) {
            //与上一条数据属于同一个省
            //再比较是不是同一个市的
            if (isSameCity(newer,old)) {
                //与上一条数据属于同一个省，并且是同一个城市
                //直辖市中的区肯定有不同的市级代码,所以这里不用判断是否是直辖市
                //拿出临时省里的城市列表，取出最后一个城市，往这个最后城市中添加区，并保存
                List<CityParseBean> tempCities = tempProvince.getCities();
                CityParseBean tempCity = tempCities.get(tempCities.size() - 1);
                List<AreaParseBean> tempBlocks = tempCity.getBlocks();
                tempBlocks.add(newer);
                tempCity.setBlocks(tempBlocks);
                tempCities.remove(tempCities.size() - 1);
                tempCities.add(tempCity);
                tempProvince.setCities(tempCities);
            } else {
                //与上一条数据属于同一个省，但不是同一个城市
                List<CityParseBean> tempCities = tempProvince.getCities();
                tempCities.add(makeNewCity(newer));
                tempProvince.setCities(tempCities);
            }
        }else{
            //与上一条数据属于不同的省，保存上一条数据到数据库，并生成新的省数据
            saveProvinceToDb(tempProvince);
            tempProvince = makeNewProvince(newer);
        }
    }
    private int i;
    private void sendProgressMsg(int i) {
        if(mHandler!=null){
            Message msg = mHandler.obtainMessage(2);
            msg.arg1 = i;msg.arg2 = 3181;
            mHandler.sendMessage(msg);
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
                sendProgressMsg(i++);
            }
            c.save();
        }
        bean.save();
    }

    private CityParseBean makeNewCity(AreaParseBean n) {
        CityParseBean c = new CityParseBean();
        c.copyValueFrom(n);
        if(!isOnlyCity(n)){//如果不是直辖，造一个区出来
            List<AreaParseBean> blocks = c.getBlocks();
            blocks.add(n);
            c.setBlocks(blocks);
        }
        return c;
    }

    private ProvinceParseBean makeNewProvince(AreaParseBean n) {
        ProvinceParseBean p = new ProvinceParseBean();
        p.copyValueFrom(n);
        List<CityParseBean> cities = p.getCities();
        cities.add(makeNewCity(n));
        p.setCities(cities);
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
