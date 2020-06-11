package com.shanji.wifi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @version: V1.0
 * @className: WiFiUtils
 * @packageName: com.shanji.wifi
 * @data: 2020/3/12 20:17
 * @description:  本工具类只适用于家里的 小米路由器，通过调用 getEquipments 可以获取当前连接到路由器的设备的 ip 地址等信息，具体可以获取的信息在本文件末尾
 *  依赖：
 *
 *     <dependency>
 *       <groupId>com.alibaba</groupId>
 *       <artifactId>fastjson</artifactId>
 *       <version>1.2.62</version>
 *     </dependency>
 */
public class WiFiUtils
{
    private static String url = "http://192.168.31.1/cgi-bin/luci/;stok=899bf688e2f04d5e3b2e23fc3e36ba33/api/misystem/devicelist";
    public static void main(String[] args)
    {
        ArrayList<Equipment> equipments = getEquipments();
        for(Equipment temp : equipments)
        {
            System.out.println(temp);
        }
    }
    public static ArrayList<Equipment> getEquipments()
    {
        String info = getHttpRequestData(url);
        JSONObject jsonObject = JSON.parseObject(info);
        JSONArray list = jsonObject.getJSONArray("list");
        ArrayList<Equipment> equipments = new ArrayList<>();
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext())
        {
            JSONObject next = (JSONObject)iterator.next();
            Equipment temp = new Equipment();
            temp.setIp(next.getJSONArray("ip").getJSONObject(0).getString("ip"));
            temp.setMac(next.getString("mac"));
            temp.setName(next.getString("name"));
            equipments.add(temp);
        }
        return equipments;
    }


    private static String getHttpRequestData(String urlPath) {

        // 首先抓取异常并处理
        StringBuilder returnString = new StringBuilder();
        try{
            // 代码实现以GET请求方式为主,POST跳过
            /** 1 GET方式请求数据 start*/

            // 1  创建URL对象,接收用户传递访问地址对象链接
            URL url = new URL(urlPath);

            // 2 打开用户传递URL参数地址
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            // 3 设置HTTP请求的一些参数信息
            connect.setRequestMethod("GET"); // 参数必须大写
            connect.connect();

            // 4 获取URL请求到的数据，并创建数据流接收
            InputStream isString = connect.getInputStream();

            // 5 构建一个字符流缓冲对象,承载URL读取到的数据
            BufferedReader isRead = new BufferedReader(new InputStreamReader(isString));

            // 6 输出打印获取到的文件流
            String str = "";
            while ((str = isRead.readLine()) != null) {
                str = new String(str.getBytes(),"UTF-8"); //解决中文乱码问题
                returnString.append(str);
            }

            // 7 关闭流
            isString.close();
            connect.disconnect();

            // 8 JSON转List对象
            // do somthings


        }catch(Exception e){
            e.printStackTrace();
        }

        return returnString.toString();
    }
}
/*
*
* {
        "mac": "A8:9C:ED:22:90:3D",
        "oname": "RedmiK20-aojiaoxiaos",
        "isap": 0,
        "parent": "",
        "authority": {
            "wan": 1,
            "pridisk": 0,
            "admin": 1,
            "lan": 1
        },
        "push": 1,
        "online": 1,
        "name": "RedmiK20-aojiaoxiaos",
        "times": 0,
        "ip": [
            {
                "downspeed": "0",
                "online": "6150",
                "active": 1,
                "upspeed": "0",
                "ip": "192.168.31.122"
            }
        ],
        "statistics": {
            "downspeed": "0",
            "online": "6150",
            "upspeed": "0"
        },
        "icon": "",
        "type": 1
},
*
* */