package com.shanji.net;

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @version: V1.0
 * @className: NetUtil
 * @packageName: com.shanji.net
 * @data: 2020/3/9 16:14
 * @description:
 *
 *      通过调用 getInet4Address 方法获取真正的IP地址，getMask 获取子网掩码
 *
 */
public class NetUtil
{
    public static void main(String[] args) throws Exception
    {
        String ipAddress = getInet4Address();
        String mask = getSubNetMask(ipAddress);
        System.out.println(ipAddress);
    }
    public static String getMask()
    {
        String ipAddress = getInet4Address();
        return getSubNetMask(ipAddress);
    }
    private static String getSubNetMask(String ipAddress)
    {
        try {
            List<String> list = new ArrayList<String>();
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                List<InterfaceAddress> faceAddresses = ni.getInterfaceAddresses();
                if (faceAddresses == null) {
                    faceAddresses = Collections.EMPTY_LIST;
                }
                //
                for (InterfaceAddress faceAddress : faceAddresses) {
                    InetAddress address = faceAddress.getAddress();
                    if (address.isLoopbackAddress() == true || address.getHostAddress().contains(":")) {
                        continue;
                    }
                    //
                    byte[] ipBytes = address.getAddress();
                    long ipData = (ipBytes[0] << 24) + (ipBytes[1] << 16) + (ipBytes[2] << 8) + (ipBytes[3]);
                    String ipMask = calcMaskByPrefixLength(faceAddress.getNetworkPrefixLength());
                    //
                    //                    long a =4294967295L;
                    //
//                    System.out.println(ipMask + "\t" + address.getHostAddress());
                    if (ipAddress.equals(address.getHostAddress()))
                    {
                        return ipMask;
                    }
                    //
                }
            }
            return null;
        } catch (Throwable t) {
            return null;
        }
    }

    private static String calcMaskByPrefixLength(int length) {
        int mask = -1 << (32 - length);
        int partsNum = 4;
        int bitsOfPart = 8;
        int maskParts[] = new int[partsNum];
        int selector = 0x000000ff;
        for (int i = 0; i < maskParts.length; i++) {
            int pos = maskParts.length - 1 - i;
            maskParts[pos] = (mask >> (i * bitsOfPart)) & selector;
        }
        String result = "";
        result = result + maskParts[0];
        for (int i = 1; i < maskParts.length; i++) {
            result = result + "." + maskParts[i];
        }
        return result;
    }

    public static String getInet4Address(){
        try {
            //获取所有网络接口
            Enumeration<NetworkInterface> allNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            //遍历所有网络接口
            for(;allNetworkInterfaces.hasMoreElements();){
                NetworkInterface networkInterface=allNetworkInterfaces.nextElement();
                //如果此网络接口为 回环接口 或者 虚拟接口(子接口) 或者 未启用 或者 描述中包含VM
                if(networkInterface.isLoopback()||networkInterface.isVirtual()||!networkInterface.isUp()||networkInterface.getDisplayName().contains("VM")){
                    //继续下次循环
                    continue;
                }
                //如果不是Intel与Realtek的网卡
//                if(!(networkInterface.getDisplayName().contains("Intel"))&&!(networkInterface.getDisplayName().contains("Realtek"))){
//                         //继续下次循环
//                            continue;
//                }
                //遍历此接口下的所有IP（因为包括子网掩码各种信息）
                for(Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses(); inetAddressEnumeration.hasMoreElements();){
                    InetAddress inetAddress=inetAddressEnumeration.nextElement();
                    //如果此IP不为空
                    if(inetAddress!=null){
                        //如果此IP为IPV4 则返回
                        if(inetAddress instanceof Inet4Address){
                            return inetAddress.getHostAddress();
                        }
                        /*
                       // -------这样判断IPV4更快----------
                        if(inetAddress.getAddress().length==4){
                            return inetAddress;
                        }

                         */

                    }
                }


            }
            return null;

        }catch(SocketException e){
            e.printStackTrace();
            return null;
        }
    }
}
