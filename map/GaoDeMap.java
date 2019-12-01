package com.shanji.over.example;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @version: V1.0
 * @className: GaoDeMap
 * @packageName: com.shanji.over.example
 * @data: 2019/12/1 12:21
 * @description: 通过高德地图的API来转换
 *      需要环境：
 *          1、一个有WEB服务API权限的高德地图apikey
 *          2、<dependency>
 *                  <groupId>com.alibaba</groupId>
 *                  <artifactId>fastjson</artifactId>
 *                  <version>1.2.58</version>
 *              </dependency>
 */
public class GaoDeMap
{


    //高德地图apikey
    private final static String KEY = "d7960fb02bf1049bd4d0f1f1448e0d0f";
    //将地址转为经纬度的请求url
    private static String ADDRESS_TO_SOME_URL = "http://restapi.amap.com/v3/geocode/geo?key=" + KEY +"&address=";
    //将经纬度转为地址的请求url
    private static String LAT_LOG_TO_ADDRESS_URL = "http://restapi.amap.com/v3/geocode/regeo?key=" + KEY + "&location=";

    /**
     *
     * @param address：地址
     * @return 经纬度中间用','隔开
     */
    public static String addressTranslationLatitudeAndLongitude(String address) {
        //"http://restapi.amap.com/v3/geocode/geo?address=上海市东方明珠&output=JSON&key=xxxxxxxxx";
        String geturl = ADDRESS_TO_SOME_URL + address;
        String location = "";
        try {
            JSONObject a = JSON.parseObject(getConnection(geturl));
            JSONArray sddressArr = JSON.parseArray(a.get("geocodes").toString());
            JSONObject c = JSON.parseObject(sddressArr.get(0).toString());
            location = c.get("location").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     *
     * @param longitude 经度
     * @param dimension 维度
     * @return  详细地址
     */
    public static String latitudeAndLongitudeTranslationAddress(String longitude,String dimension)
    {
        String geturl = LAT_LOG_TO_ADDRESS_URL + longitude + "," + dimension;
        String location = "";
        try {
            JSONObject a = JSON.parseObject(getConnection(geturl));
            if(a.get("status").equals("1"))
            {
                JSONObject regeocode = (JSONObject) a.get("regeocode");
                return regeocode.get("formatted_address").toString();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }


    /**
     *
     * @param url 请求URL
     * @return 请求给定的url，并将结果转为字符串返回
     * @throws Exception
     */
    public static String getConnection(String url) throws Exception
    {
        URL urls = new URL(url);    // 把字符串转换为URL请求地址
        HttpURLConnection connection = (HttpURLConnection) urls.openConnection();// 打开连接
        connection.connect();// 连接会话
        // 获取输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {// 循环读取流
            sb.append(line);
        }
        br.close();// 关闭流
        connection.disconnect();// 断开连接
        return sb.toString();
    }

    /*public static void main(String[] args)
    {
        System.out.println(latitudeAndLongitudeTranslationAddress("116.481488","39.990464"));
        System.out.println(addressTranslationLatitudeAndLongitude("北京市朝阳区望京街道方恒国际中心B座方恒国际中心"));
    }*/
}
