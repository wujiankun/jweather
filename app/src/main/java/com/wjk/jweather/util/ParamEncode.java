package com.wjk.jweather.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import sun.misc.BASE64Encoder;

/**
 * Created by wujiankun on 2017/11/8.
 * key 值加密
 */

public class ParamEncode {
    /**
     * 和风天气签名生成算法-JAVA版本
     * @param params 请求参数集，所有参数必须已转换为字符串类型
     * @param secret 签名密钥
     * @return 签名
     * @throws IOException
     */
    public static String getSignature(HashMap params, String secret) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        TreeMap sortedParams = new TreeMap(params);
        Set<Map.Entry<String,String>> entrySet = sortedParams.entrySet();
        StringBuilder baseString = new StringBuilder();
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        for (Map.Entry<String,String> param : entrySet) {
            //sign参数 和 空值参数 不加入算法
            if(param.getValue()!=null && !"".equals(param.getKey().trim()) && !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue().trim())) {
                baseString.append(param.getKey().trim()).append("=").append(param.getValue().trim()).append("&");
            }
        }
        if(baseString.length() > 0 ) {
            baseString.deleteCharAt(baseString.length() - 1).append(secret);
        }

        // 使用MD5对待签名串求签
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(baseString.toString().getBytes("UTF-8"));
            return new BASE64Encoder().encode(bytes);
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }
    }
}
