
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;

import java.io.File;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 *          <dependency>
 *             <groupId>cn.hutool</groupId>
 *             <artifactId>hutool-all</artifactId>
 *             <version>5.5.7</version>
 *         </dependency>
 *
 *         <dependency>
 *             <groupId>commons-net</groupId>
 *             <artifactId>commons-net</artifactId>
 *             <version>3.6</version>
 *         </dependency>
 *
 *
 *
 */

public class FtpUtil
{

    private static String IP_ADDR = "";
    private static int PORT = 21;
    private static String USERNAME = "";
    private static String PASSWORD = "";

    public static String upload(String file,String project) throws Exception
    {
        return upload(new File(file),project);
    }

    public static String upload(File file, String project) throws Exception
    {
        LocalDateTime time=LocalDateTime.now();

        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        String strDate2 = dtf2.format(time);


        Ftp ftp = new Ftp(IP_ADDR,PORT,USERNAME,PASSWORD);

        ftp.getClient().enterLocalPassiveMode();   // 设置被动模式

        String path = project + "/" + USERNAME + "/" + strDate2;

        String newName = dtf3.format(time) + file.getName().substring(file.getName().lastIndexOf("."));


        ftp.upload(path,newName, FileUtil.file(file));
        ftp.close();
        return path + "/" + newName;
    }

    public static void download(String path,String file) throws Exception
    {
        Ftp ftp = new Ftp(IP_ADDR,PORT,USERNAME,PASSWORD);
        ftp.getClient().enterLocalPassiveMode();
        System.out.println(path.substring(0, path.lastIndexOf("/")));
        System.out.println(path.substring(path.lastIndexOf("/") + 1));
        ftp.download(path.substring(0,path.lastIndexOf("/")),path.substring(path.lastIndexOf("/") + 1), FileUtil.file(file));
        ftp.close();
    }

    public static void download(String path,OutputStream ouput) throws Exception
    {
        Ftp ftp = new Ftp(IP_ADDR,PORT,USERNAME,PASSWORD);
        ftp.getClient().enterLocalPassiveMode();
        ftp.download(path.substring(0,path.lastIndexOf("/")),path.substring(path.lastIndexOf("/") + 1),ouput);
        ftp.close();
    }

    public static void main(String[] args) throws Exception {


        String path = upload("E:/pic/宋祖儿/20191016164525.jpg", "test");
        download(path,"E:/1.jpg");
    }
}
