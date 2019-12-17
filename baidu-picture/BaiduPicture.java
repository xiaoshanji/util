package com.shanji.over.example;



import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.*;


/**
 * 爬去百度图片
 *      尺寸：1920 * 1080
 *      分页：一页 30 张图片
 *      爬取后存入地址：E:\pic
 *      需要依赖：
 *      <dependency>
 *       <groupId>org.apache.commons</groupId>
 *       <artifactId>commons-lang3</artifactId>
 *       <version>3.4</version>
 *      </dependency>
 *      <dependency>
 *       <groupId>org.jsoup</groupId>
 *       <artifactId>jsoup</artifactId>
 *       <version>1.8.3</version>
 *     </dependency>
 */

public class BaiduPicture
{
    public static void main(String[] args) throws Exception{

        /**
         * 爬去百度图片
         */
        String downloadPath = "E:\\pic";

        System.out.println("输入爬取关键字（可用空格，、号分隔多个想爬的关键字）：");
        Scanner KeyWord = new Scanner(System.in);
        String Word =KeyWord.nextLine();

        System.out.println("输入爬取页数：");
        int i = KeyWord.nextInt();
        KeyWord.nextLine();
        System.out.println("是否下载高清");
        boolean flag = false;

        String temp = KeyWord.nextLine();

        if(temp.equalsIgnoreCase("y"))
        {
            flag = true;
        }
        else
        {
            flag = false;
        }

        List<String> list = nameList(Word);
        getPictures(list,i,downloadPath,flag); //1代表下载一页，一页一般有30张图片

        /*
        模拟window的dos控制台
        通过输入命令，然后调用操作的系统的系统指令

        Scanner console = new Scanner(System.in);
        final String exit = "exit";
        System.out.print("输入dos命令：");
        String param = console.nextLine();
        while (!param.equalsIgnoreCase(exit))
        {
            try {
                System.out.println(param);
                Runtime rt = Runtime.getRuntime();
                Process pr = rt.exec("cmd /c " + param);
                //Process pr = rt.exec("D:\\xunlei\\project.aspx");
                BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
                String line = null;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                int exitVal = pr.waitFor();
                System.out.println("Exited with error code " + exitVal);

            } catch (Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
            System.out.print("输入dos命令：");
            param = console.nextLine();
        }*/
    }

    public static void getPictures(List<String> keywordList, int max,String downloadPath,boolean flag) throws Exception{ // key为关键词,max作为爬取的页数
        String gsm=Integer.toHexString(max)+"";
        String finalURL = "";
        String tempPath = "";
        for(String keyword : keywordList){
            tempPath = downloadPath;
            if(!tempPath.endsWith("\\")){
                tempPath = downloadPath+"\\";
            }
            tempPath = tempPath+keyword+"\\";
            File f = new File(tempPath);
            if(!f.exists()){
                f.mkdirs();
            }
            int picCount = 1;
            for(int page=0; page < max;page++) {
                sop("正在下载第"+page+"页面");
                Document document = null;
                try {
//                    String url ="http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word="+keyword+"&cg=star&pn="+page*30+"&rn=30&itg=0&z=0&fr=&width=1920&height=1080&lm=-1&ic=0&s=0&st=-1&gsm="+Integer.toHexString(page*30);
                    String url = "";
                    if(flag)
                    {
                        url = "https://image.baidu.com/search/index?ct=201326592&z=&tn=baiduimage&ipn=r&word=" + keyword + "&pn=" + page*30 + "&istype=2&ie=utf-8&oe=utf-8&cl=2&lm=-1&st=-1&fr=&fmq=&ic=0&se=&sme=&width=1920&height=1080&face=0&hd=1";
                    }
                    else
                    {
                        url = "https://image.baidu.com/search/index?ct=201326592&z=&tn=baiduimage&ipn=r&word=" + keyword + "&pn=" + page*30 + "&istype=2&ie=utf-8&oe=utf-8&cl=2&lm=-1&st=-1&fr=&fmq=&ic=0&se=&sme=&width=1920&height=1080&face=0";
                    }
                    sop(url);
                    document = Jsoup.connect(url).data("query", "Java")//请求参数
                            .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")//设置urer-agent  get();
                            .timeout(5000)
                            .get();
                    String xmlSource = document.toString();
                    xmlSource = StringEscapeUtils.unescapeHtml3(xmlSource);
//                    sop(xmlSource);
                    String reg = "objURL\":\"http://.+?\\.jpg";
                    Pattern pattern = Pattern.compile(reg);
                    Matcher m = pattern.matcher(xmlSource);
                    while (m.find()) {
                        finalURL = m.group().substring(9);
                        sop(keyword+picCount+++":"+finalURL);
                        download(finalURL,tempPath);
                        sop("下载成功");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        sop("下载完毕");
        delMultyFile(downloadPath);
        sop("已经删除所有空图");
    }
    public static void delMultyFile(String path){
        File file = new File(path);
        if(!file.exists())
        { throw new RuntimeException("File \""+path+"\" NotFound when excute the method of delMultyFile()....");}
        File[] fileList = file.listFiles();
        File tempFile=null;
        for(File f : fileList){
            if(f.isDirectory()){
                { delMultyFile(f.getAbsolutePath());}
            }else{
                if(f.length()==0)
                { sop(f.delete()+"---"+f.getName());}
            }
        }
    }
    public static List<String> nameList(String nameList){
        List<String> arr = new ArrayList<String>();
        String[] list;
        if(nameList.contains(","))
        { list= nameList.split(",");}
        else if(nameList.contains("、"))
        { list= nameList.split("、");}
        else if(nameList.contains(" "))
        {list= nameList.split(" ");}
        else{
            arr.add(nameList);
            return arr;
        }
        for(String s : list){
            arr.add(s);
        }
        return arr;
    }
    public static void sop(Object obj){
        System.out.println(obj);
    }
    //根据图片网络地址下载图片
    public static void download(String url,String path){
        //path = path.substring(0,path.length()-2);
        File file= null;
        File dirFile=null;
        FileOutputStream fos=null;
        HttpURLConnection httpCon = null;
        URLConnection con = null;
        URL urlObj=null;
        InputStream in =null;
        byte[] size = new byte[1024];
        int num=0;
        try {
            String downloadName = url.split(",")[0].replace("\"","");
//            String downloadName= url.substring(url.lastIndexOf("/")+1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            downloadName = dateFormat.format(new Date()) + downloadName.substring(downloadName.lastIndexOf("."));

            dirFile = new File(path);
            if(!dirFile.exists() && path.length()>0){
                if(dirFile.mkdir()){
                    sop("creat document file \""+path.substring(0,path.length()-1)+"\" success...\n");
                }
            }else{
                file = new File(path+downloadName);
                fos = new FileOutputStream(file);
                if(url.startsWith("http")){
                    String temp = url.split(",")[0].replace("\"","");
                    urlObj = new URL(temp);
                    con = urlObj.openConnection();
                    httpCon =(HttpURLConnection) con;
                    in = httpCon.getInputStream();
                    while((num=in.read(size)) != -1){
                        for(int i=0;i<num;i++)
                        {  fos.write(size[i]);}
                    }
                }
            }
        }catch (FileNotFoundException notFoundE) {
            sop("找不到该网络图片....");
        }catch(NullPointerException nullPointerE){
            sop("找不到该网络图片....");
        }catch(IOException ioE){
            sop("产生IO异常.....");
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
