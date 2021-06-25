package com.shanji.email;


import com.aliyun.dm20151123.Client;
import com.aliyun.dm20151123.models.SingleSendMailRequest;
import com.aliyun.teaopenapi.models.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: V1.0
 * @className: aliEmail
 * @packageName: com.shanji.email
 * @data: 2021/6/25 16:10
 * @description: 阿里云邮件推送工具
 *  <dependency>
 *             <groupId>com.aliyun</groupId>
 *             <artifactId>dm20151123</artifactId>
 *             <version>1.0.0</version>
 *         </dependency>
 *
 *
 *        <repository>
 *             <id>sonatype-nexus-staging</id>
 *             <name>Sonatype Nexus Staging</name>
 *             <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
 *             <releases>
 *                 <enabled>true</enabled>
 *             </releases>
 *             <snapshots>
 *                 <enabled>true</enabled>
 *             </snapshots>
 *         </repository>
 */
public class aliEmail
{

    public static Client createClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(aliEmailConfig.ACCESSKEYID)
                // 您的AccessKey Secret
                .setAccessKeySecret(aliEmailConfig.ACCESSKEYSCERET);
        // 访问的域名
        config.endpoint = "dm.aliyuncs.com";
        return new Client(config);
    }

    // 单个推送
    public static boolean sendSingle(String name,String toAddress,String subject,String message) throws Exception
    {
        Client client = createClient();
        return send(client,name,toAddress,subject,message);
    }

    // 批量推送
    public static Map<String,Boolean> sendBatch(String name,String[] address,String subject,String message) throws Exception
    {
        Map<String,Boolean> result = new HashMap<>();
        Client client = createClient();
        for(String temp : address)
        {
            boolean send = send(client,name, temp, subject, message);
            result.put(temp,send);
        }
        return result;
    }

    // 推送
    private static boolean send(Client client,String name,String toAddress,String subject,String message) throws Exception
    {
        SingleSendMailRequest singleSendMailRequest = new SingleSendMailRequest()
                .setAccountName(aliEmailConfig.ACCOUNT_NAME)  // 配置的邮件推送的发信地址
                .setAddressType(0)
                .setReplyToAddress(true)
                .setToAddress(toAddress)
                .setSubject(subject)
                .setFromAlias(name)
                .setHtmlBody(message);
        try
        {
            client.singleSendMail(singleSendMailRequest);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
}
