package com.shanji.img;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;

/**
 * @version: V1.0
 * @className: ImageUtil
 * @packageName: com.shanji.img
 * @data: 2020/8/10 12:59
 * @description: 压缩图片
 *
 *  <dependency>
 *             <groupId>net.coobird</groupId>
 *             <artifactId>thumbnailator</artifactId>
 *             <version>0.4.8</version>
 *         </dependency>
 *
 */
public class ImgeUtil
{
    public static void main(String[] args) throws Exception
    {
        commpressPicForSize("C:/Users/Administrator/Desktop/1636866551002.jpg","C:/Users/Administrator/Desktop/1636866551002-1.jpg");
    }


    public static void commpressPicForSize(String srcPath,String desPath) throws Exception
    {
        File src = new File(srcPath);
        File des = new File(desPath);
        commpressPicForSize(src,des);
    }

    public static void commpressPicForSize(File src,File des) throws Exception
    {
        Thumbnails.of(src).scale(1f).outputQuality(0.25f).toFile(des);
    }
}
