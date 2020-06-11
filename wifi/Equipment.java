package com.shanji.wifi;

/**
 * @version: V1.0
 * @className: Equipment
 * @packageName: com.shanji.wifi
 * @data: 2020/5/13 18:44
 * @description:
 */
public class Equipment
{
    private String name;
    private String ip;
    private String mac;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString()
    {
        return "name：" + name + "\tip：" + ip + "\tmac：" + mac;
    }
}
