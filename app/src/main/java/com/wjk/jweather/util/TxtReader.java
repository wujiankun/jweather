package com.wjk.jweather.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
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




}
