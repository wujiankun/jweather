package com.wjk.jweather.view.temp;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * txt文件读取
 */

public class TxtReader {

    public static List<String> parseText(String srtPath) {
        String charset = get_charset(srtPath);
        FileInputStream inputStream = null;
        BufferedReader br = null;
        String line = null;
        try {
            inputStream = new FileInputStream(srtPath);
            br = new BufferedReader(new InputStreamReader(inputStream, charset));
            List<String> result = new ArrayList<>();
            result.add("");
            while ((line = br.readLine()) != null) {
                result.add(line);
                Log.e("wjk",line);
            }
            result.add("");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (Exception e){}
            }
            if(br!=null) {
                try {
                    br.close();
                } catch (Exception e){}
            }
        }
        return null;
    }

    private static String get_charset(String file) {
        String charset = "UTF-8";
        byte[] first3Bytes = new byte[3];
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset;
            }
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }



    public static void readTextAndSaveToDb(Context context) {
        AssetManager am=context.getAssets();
        try {
            InputStream is=am.open("city-list.txt");

            String code=TxtReader.getCode(is);
            is=am.open("city-list.txt");
            BufferedReader br=new BufferedReader(new InputStreamReader(is,code));
            String line=br.readLine();
            while(line!=null){
                line=br.readLine();
                String[] s = line.split("\t");
                CityParseBean bean = new CityParseBean();
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCode(InputStream is){
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
