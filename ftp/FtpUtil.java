package com.shanji.ftp;

import com.shanji.ftp.entity.FtpEntity;
import com.shanji.project.ProjectUtil;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.time.LocalDate;

/** 本工具类用于 FTP 上传文件，首先对 FtpEntity 的属性进行赋值，然后调用 connect 连接ftp，作为上传方法的参数，在上传完文件后要调用 close 方法断开连接。对于web 项目的图片显示
 *
 *  <dependency>
 *       <groupId>commons-net</groupId>
 *       <artifactId>commons-net</artifactId>
 *       <version>3.6</version>
 *     </dependency>
 *
 *    如果应用于 web 项目 还需要导入 servlet 的依赖，以及工具类中的 project 包
 *
 */

public class FtpUtil
{
    /**
     *
     * @param
     * @return
     * @throws Exception
     */
    public static FTPSClient connectFtp() throws Exception
    {
        FTPSClient ftp = new FTPSClient();
        int reply;
        if(FtpEntity.Port == null)
        {
            //没有指定端口时，用默认的21端口。
            ftp.connect(FtpEntity.IpAddr,21);
        }
        else
        {
            ftp.connect(FtpEntity.IpAddr,FtpEntity.Port);
        }
        ftp.login(FtpEntity.UserName,FtpEntity.Password);
        ftp.execPBSZ(0);
        ftp.execPROT("P");
        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTPSClient.BINARY_FILE_TYPE);
        ftp.setControlEncoding("UTF-8");
        reply = ftp.getReplyCode();
        if(!FTPReply.isPositiveCompletion(reply))
        {
            ftp.disconnect();
            return null;
        }
        return ftp;
    }

    /**
     * 关闭ftp连接
     * @throws Exception
     */
    public static void closeFtp(FTPSClient ftp) throws Exception
    {
        if(ftp != null && ftp.isConnected())
        {
            ftp.logout();
            ftp.disconnect();
        }
    }

    /**
     * 上传文件，先连接，在上传文件，返回在ftp服务器中的路径。
     *
     * @throws Exception
     */
    public static String  upLoadFile(File file,FTPSClient ftp,String serverPath) throws Exception
    {

        if(!ftp.changeWorkingDirectory(serverPath))
        {
            createDir(serverPath,ftp);
        }

        if(file.isDirectory())
        {
            ftp.makeDirectory(file.getName());
            ftp.changeWorkingDirectory(file.getName());
            File[] files = file.listFiles();
            for(File temp : files)
            {
                if(temp.isDirectory())
                {
                    upLoadFile(temp,ftp,serverPath + "/" +file.getName());
                }
                else
                {
                    FileInputStream input = new FileInputStream(temp);
                    ftp.storeFile(temp.getName(),input);
                    input.close();
                }
            }
            return  serverPath +"/" + file.getName();
        }
        else
        {
            FileInputStream input = new FileInputStream(file);
            ftp.storeFile(file.getName(),input);
            input.close();
            return serverPath + "/" + file.getName();
        }
    }


    /**
     * 上传文件，用于 web 项目。
     *
     * @throws Exception
     */
    public static String  upLoadFile(HttpServletRequest request , HttpServletResponse response) throws Exception
    {
        String projectName = ProjectUtil.getProjectName(request);
        String path = "/" + projectName + LocalDate.now();
        Part file = request.getPart("fileName");
        String fileName = file.getSubmittedFileName();

        String ext = fileName.substring(fileName.indexOf("."));

        /**
         * 在此位置按照 规则 重命名文件，并赋值给 fileName
         *
         *
         */

        FTPSClient ftpsClient = connectFtp();

        if(!ftpsClient.changeWorkingDirectory(path))
        {
            createDir(path,ftpsClient);
        }

        ftpsClient.storeFile(fileName,file.getInputStream());
        closeFtp(ftpsClient);

        return path + "/" + fileName;
    }

    /**
     * Description: 从 ftp 上获取文件 并以流的形式返回给前端，用于 web 项目，需要以 filePath 作为 name 进行传值
     *
     * @return
     */
    public static void downloadFile(HttpServletRequest request , HttpServletResponse response) throws Exception
    {
        String filePath = request.getParameter("filePath");   // 通过request 获取到文件url
        String ext = filePath.substring(filePath.indexOf("."));
        FTPSClient ftp = connectFtp();

        InputStream inputStream = ftp.retrieveFileStream(filePath);
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());

        if(ext.equals("jpg"))
        {
            response.setContentType("image/jpeg");
        }else if(ext.equals("JPG"))
        {
            response.setContentType("image/jpeg");
        }else if(ext.equals("png"))
        {
            response.setContentType("image/png");
        }else if(ext.equals("PNG"))
        {
            response.setContentType("image/png");
        }

        byte[] buff =new byte[1024];
        //所读取的内容使用n来接收
        int n;
        //当没有读取完时,继续读取,循环
        while(inputStream.read(buff) != -1)
        {
            //将字节数组的数据全部写入到输出流中
            outputStream.write(buff);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
        closeFtp(ftp);
    }


    /**
     * 进入 用户名/日期 的文件夹，如果没有则创建并进入
     * @param dir
     * @return
     */
    private static boolean createDir(String dir,FTPSClient ftp) throws Exception
    {
        if(isNullOrEmpty(dir))
        {
            return true;
        }
        String d;
        //目录编码，解决中文路径问题
        d = new String(dir.toString().getBytes("GBK"),"iso-8859-1");
        //尝试切入目录
        if(ftp.changeWorkingDirectory(d))
            return true;
        dir = trimStart(dir, "/");
        dir = trimEnd(dir, "/");
        String[] arr =  dir.split("/");
        StringBuffer sbfDir=new StringBuffer();
        //循环生成子目录
        for(String s : arr)
        {
            sbfDir.append("/");
            sbfDir.append(s);
            //目录编码，解决中文路径问题
            d = new String(sbfDir.toString().getBytes("GBK"),"iso-8859-1");
            //尝试切入目录
            if(ftp.changeWorkingDirectory(d))
                continue;
            if(!ftp.makeDirectory(d)){
                System.out.println("[失败]ftp创建目录："+sbfDir.toString());
                return false;
            }
            System.out.println("[成功]创建ftp目录："+sbfDir.toString());
        }
        //将目录切换至指定路径
        return ftp.changeWorkingDirectory(d);
    }

    /**
     * 去掉最前面的 /
     * @param str
     * @param trim
     * @return
     */
    private static String trimStart(String str,String trim)
    {
        if(str==null)
            return null;
        return str.replaceAll("^(" + trim + ")+", "");
    }

    /**
     * 去掉最后面的  /
     * @param str
     * @param trim
     * @return
     */
    private static String trimEnd(String str,String trim)
    {
        if(str==null)
        {
            return null;
        }
        return str.replaceAll("("+trim+")+$", "");
    }

    /**
     * 判断路径是否为空
     * @param str
     * @return
     */
    private static boolean isNullOrEmpty(String str)
    {
        return str==null || str.trim().isEmpty();
    }
}
