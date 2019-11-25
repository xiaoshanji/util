package ftp;

import ftp.entity.FtpEntity;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FtpUtil
{
    private static FTPSClient ftp;


    /**
     * 连接ftp，ftpEntity是用ip,端口，账号，密码实例化的实例
     * @param ftpEntity
     * @return
     * @throws Exception
     */
    public static boolean connectFtp(FtpEntity ftpEntity) throws Exception
    {
        ftp = new FTPSClient();
        boolean flag = false;
        int reply;
        if(ftpEntity.getPort() == null)
        {
            //没有指定端口时，用默认的21端口。
            ftp.connect(ftpEntity.getIpAddr(),21);
        }
        else
        {
            ftp.connect(ftpEntity.getIpAddr(),ftpEntity.getPort());
        }
        ftp.login(ftpEntity.getUserName(),ftpEntity.getPassword());
        ftp.execPBSZ(0);
        ftp.execPROT("P");
        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftp.setControlEncoding("UTF-8");
        reply = ftp.getReplyCode();
        if(!FTPReply.isPositiveCompletion(reply))
        {
            ftp.disconnect();
            return flag;
        }
        //进入 用户名/日期 的文件夹，如果没有则创建，并进入。
        createDir(ftpEntity.getPath());
        flag = true;
        return flag;
    }

    /**
     * 关闭ftp连接
     * @throws Exception
     */
    public static void closeFtp() throws Exception
    {
        if(ftp != null && ftp.isConnected())
        {
            ftp.logout();
            ftp.disconnect();
        }
    }

    /**
     * 上传文件，先连接，在上传文件，返回在ftp服务器中的路径。
     * @param file：上传的文件
     * @param entity：用于连接ftp
     * @throws Exception
     */
    public static String  upLoad(File file,FtpEntity entity) throws Exception
    {
        connectFtp(entity);
        upLoad(file);
        closeFtp();
        return entity.getPath() + "/" + file.getName();
    }

    /**
     * 上传文件，file如果是文件，那么返回的就是文件名，如果是目录返回时目录名
     * @param file
     * @throws Exception
     */
    public static String upLoad(File file) throws Exception
    {
        if(file.isDirectory())
        {
            ftp.makeDirectory(file.getName());
            ftp.changeWorkingDirectory(file.getName());
            String[] files = file.list();
            for(String fstr : files)
            {
                File childFile = new File(file.getPath() + "/" + fstr);
                if(childFile.isDirectory())
                {
                    upLoad(childFile);
                    ftp.changeToParentDirectory();
                }
                else
                {
                    File secondChildFile = new File(file.getPath() + "/" + fstr);
                    FileInputStream input = new FileInputStream(secondChildFile);
                    ftp.storeFile(secondChildFile.getName(),input);
                    input.close();
                }
            }
            return  file.getName();
        }
        else
        {
            File childFile = new File(file.getPath());
            FileInputStream input = new FileInputStream(childFile);
            ftp.storeFile(childFile.getName(),input);
            input.close();
            return childFile.getName();
        }
    }

    /**
     * 进入 用户名/日期 的文件夹，如果没有则创建并进入
     * @param dir
     * @return
     */
    public static boolean createDir(String dir){
        if(isNullOrEmpty(dir))
            return true;
        String d;
        try {
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
            for(String s : arr){
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 去掉最前面的 /
     * @param str
     * @param trim
     * @return
     */
    public static String trimStart(String str,String trim)
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
    public static String trimEnd(String str,String trim){
        if(str==null)
            return null;
        return str.replaceAll("("+trim+")+$", "");
    }

    /**
     * 判断路径是否为空
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str){
        return str==null || str.trim().isEmpty();
    }

    /**
     * 下载指定的服务器上的文件夹下的所有文件，下载到指定目录
     * @param ftpEntity：用于连接ftp
     * @param localBaseDir：指定的下载到的本地目录
     * @param remoteBaseDir：ftp服务器目录
     * @throws Exception
     */
    public static void startDown(FtpEntity ftpEntity,String localBaseDir,String remoteBaseDir) throws Exception
    {
        if(FtpUtil.connectFtp(ftpEntity))
        {
            FTPFile[] files = null;
            boolean changedDir = ftp.changeWorkingDirectory(remoteBaseDir);
            if(changedDir)
            {
                files = ftp.listFiles();
                for(int i = 0 ; i < files.length ; i++)
                {
                    downloadFile(files[i],localBaseDir,remoteBaseDir);
                }
            }
            closeFtp();
        }
    }

    /**
     * 下载服务器上的指定问价，到指定目录
     * @param ftpFile：用于连接ftp
     * @param relativeLocalPath：指定的本地目录
     * @param relativeRemotePath：ftp服务器上的文件目录
     */
    private static void downloadFile(FTPFile ftpFile,String relativeLocalPath,String relativeRemotePath)
    {
        if(ftpFile.isFile())
        {
            if(ftpFile.getName().indexOf("?") == -1)
            {
                OutputStream output = null;
                try
                {
                    File localFile = new File(relativeLocalPath + ftpFile.getName());

                    if(localFile.exists())
                    {
                        return;
                    }
                    else
                    {
                        output = new FileOutputStream(relativeLocalPath + ftpFile.getName());
                        ftp.retrieveFile(ftpFile.getName(),output);
                        output.flush();
                        output.close();
                    }
                }
                catch (Exception e)
                {

                }
                finally
                {
                    try
                    {
                        if(output != null)
                        {
                            output.close();
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
            }
        }
        else
        {
            String newLocalRelatePaht = relativeLocalPath + ftpFile.getName();
            String newRemote = new String(relativeRemotePath + ftpFile.getName().toString());
            File file = new File(newLocalRelatePaht);
            if(!file.exists())
            {
                file.mkdirs();
            }
            try
            {
                newLocalRelatePaht = newLocalRelatePaht + "/";
                newRemote = newRemote + "/";
                String currentWorkDir = ftpFile.getName().toString();
                boolean changedDir = ftp.changeWorkingDirectory(currentWorkDir);
                if(changedDir)
                {
                    FTPFile [] files = null;
                    files = ftp.listFiles();
                    for(int i = 0 ; i < files.length ; i++)
                    {
                        downloadFile(files[i],newLocalRelatePaht,newRemote);
                    }
                }
                if(changedDir)
                {
                    ftp.changeToParentDirectory();
                }
            }
            catch (Exception e)
            {

            }
        }
    }
}
