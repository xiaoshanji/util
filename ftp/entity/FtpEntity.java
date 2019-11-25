package ftp.entity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FtpEntity
{
    private String ipAddr;
    private Integer port;
    private String userName;
    private String password;
    private String path;


    public FtpEntity(String ipAddr,Integer port,String userName,String password)
    {
        this.ipAddr = ipAddr;
        this.port = port;
        this.userName = userName;
        this.password = password;
        setPath();
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath()
    {
        return path;
    }
    private void setPath() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.path = getUserName() + "/" + formatter.format(new Date());
    }
}
