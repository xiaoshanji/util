package com.shanji.over.example;


import com.fasterxml.jackson.databind.JsonNode;
import com.shanji.over.util.json.JsonUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 爬去百度图片
 *      尺寸：1920 * 1080
 *      分页：一页 30 张图片
 *      爬取后存入地址：E:\pic
 *      需要依赖：
 *      <dependency>
 *       <groupId>org.jsoup</groupId>
 *       <artifactId>jsoup</artifactId>
 *       <version>1.8.3</version>
 *     </dependency>
 */

public class BaiduPicture
{
    public static void main(String[] args) throws Exception
    {

        String path = "E:\\pic";

        Scanner input = new Scanner(System.in);
        String word = input.nextLine();
        int page = input.nextInt();

        List<String> urls = getUrl(word,page);
//        List<String> urls = new ArrayList<>();
//        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599813801&di=183068b983e3ac444c79187e160bc90a&imgtype=jpg&er=1&src=http%3A%2F%2Fuploadfile.bizhizu.cn%2Fup%2F0b%2Fd1%2Fa8%2F0bd1a87628dd7d0411204de934361f9c.jpg.source.jpg");
        downLoad(path + "/" + word ,urls);



    }

    public static List<String> getUrl(String word,int page) throws Exception
    {
        List<String> result = new LinkedList<>();
        String url = "https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&width=1920&height=1080&word=" + word + "&rn=30";
        for(int i = 0 ; i < page * 30 ;i += 30)
        {
            url = url + "&pn=" + i;
            System.out.println(url+"\n\n\n");

            result.addAll(getUrl(url));
        }
        return getFileUrl(result,word);
    }

    public static List<String> getUrl(String url) throws Exception
    {
        List<String> result = new LinkedList<>();

        Document document = Jsoup.connect(url).get();
        JsonNode body = JsonUtil.jsonStrToJsonNode(document.getElementsByTag("body").text());
        JsonNode data = body.get("data");
        Iterator<JsonNode> iterator = data.iterator();
        while (iterator.hasNext())
        {
            JsonNode node = iterator.next();
            if(node.get("os") != null)
            {
                result.add(node.get("os").asText().replace(",","%2C"));
            }
        }
        return result;
    }
    public static List<String> getFileUrl(List<String> urls,String word) throws Exception
    {
        List<String> result = new LinkedList<>();
        String url = "https://image.baidu.com/search/detail?word=" + word + "&tn=baiduimagedetail&";

        for(String temp : urls)
        {

            Document document = Jsoup.connect(url + "os=" + temp).get();
            Element hdFirstImgObj = document.getElementById("hdFirstImgObj");
            if(hdFirstImgObj != null)
            {
                String attr = hdFirstImgObj.attr("src");
                result.add(attr);
            }
        }

        return result;
    }


    public static void downLoad(String path,List<String> urls) throws Exception
    {
        File dir = new File(path);
        if(!dir.exists())
        {
            dir.mkdir();
        }
        for(String url :urls)
        {


            URL file = new URL(url);
            int index = url.lastIndexOf(".");

            HttpURLConnection conn = (HttpURLConnection)file.openConnection();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String name = dateFormat.format(new Date()) + url.substring(index);

            int code = conn.getResponseCode();
            if (code == 404 || code == 504)
            {
                conn.disconnect();
                Thread.sleep(2000);
                continue;
            }

            InputStream input = new BufferedInputStream(conn.getInputStream());
            OutputStream out = new BufferedOutputStream(new FileOutputStream(path + "/" + name));
            System.out.println("开始下载：" + url);
            System.out.println("文件大小：" + input.available() + "字节");

            byte[] bytes = new byte[1024];
            int num = 0;
            while ((num = input.read(bytes)) != -1)
            {
                for(int i = 0 ; i < num ;i++)
                {
                    out.write(bytes[i]);
                }
            }
            conn.disconnect();
            out.flush();
            input.close();
            out.close();
            System.out.println("下载完成。");
            System.out.println("\n等待下载下一个文件........");
            Thread.sleep(2000);
        }
        System.out.println("\n下载完成。。。。。");
    }

}
