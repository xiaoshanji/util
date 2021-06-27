package com.shanji.aliSMS;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.QuerySendDetailsRequest;
import com.aliyun.dysmsapi20170525.models.QuerySendDetailsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.shanji.json.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version: V1.0
 * @className: aliSMSUtil
 * @packageName: com.shanji.aliSMS
 * @data: 2020/7/18 17:43
 * @description: 阿里云短信接口：
 *      依赖：工具类中的JsonUtil以及
 *      <dependency>
 *       <groupId>com.aliyun</groupId>
 *       <artifactId>aliyun-java-sdk-core</artifactId>
 *       <version>4.5.1</version>
 *     </dependency>
 */
public class AliSMSUtil
{


    /**
     *
     * @param SignName 短信签名 id
     * @param TemplateCode 短信模板CODE
     * @param phoneNumber 电话号码
     * @param TemplateParam 短信模板变量对应的实际值
     * @return 如果是逐条发送，会返回每个号码及其发送状态，如果不是逐条发送，直接将阿里云API返回的数据封装为map返回
     * @throws Exception
     * @description 使用同样的短信签名和模板给一个或多个电话发送短信
     */
    public static boolean sendSms(String SignName, String TemplateCode, String phoneNumber, Map<String,String> TemplateParam) throws Exception
    {

        Client client = getClient();
        return send(client,SignName,TemplateCode,phoneNumber,TemplateParam);
    }


    private static boolean send(Client client,String SignName, String TemplateCode, String phoneNumber, Map<String,String> TemplateParam) throws Exception
    {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName(SignName)
                .setTemplateCode(TemplateCode)
                .setTemplateParam(JsonUtil.ToJsonStr(TemplateParam));
        SendSmsResponse response = client.sendSms(sendSmsRequest);
        String code = response.getBody().getCode();
        if(code.equals("OK"))
        {
            return true;
        }
        return false;
    }


    /**
     *
     * @param SignNames 短信签名名称，String 数组格式。
     * @param TemplateCode 短信模板CODE
     * @param phoneNumbers 接收短信的手机号码，String 数组格式。
     * @param TemplateParam 短信模板变量对应的实际值,且模板变量值的个数必须与手机号码、签名的个数相同、内容一一对应，
     *                      表示向指定手机号码中发对应签名的短信，且短信模板中的变量参数替换为对应的值.
     * @return
     * @throws Exception
     * @description 短信批量发送接口，支持在一次请求中分别向多个不同的手机号码发送不同签名的短信。手机号码等参数均为JSON格式，字段个数相同，
     *      一一对应，短信服务根据字段在JSON中的顺序判断发往指定手机号码的签名。
     */
    public static Map<String,Boolean> sendBatchSms(String[] SignNames, String TemplateCode, String[] phoneNumbers, List<Map<String,String>> TemplateParam) throws Exception
    {
        Client client = getClient();

        Map<String,Boolean> result = new HashMap<>();
        for(int i = 0,len = phoneNumbers.length ; i < len ; i++)
        {
            boolean send = send(client, SignNames[i], TemplateCode, phoneNumbers[i], TemplateParam.get(i));
            result.put(phoneNumbers[i],send);
        }
        return result;

    }


    /**
     *
     * @param phoneNumber 接收短信的手机号码。
     * @param sendDate 短信发送日期，支持查询最近30天的记录。格式为yyyyMMdd
     * @param pageSize 分页查看发送记录，指定每页显示的短信记录数量。
     * @param currentPage 分页查看发送记录，指定发送记录的的当前页码。
     * @return
     * @throws Exception
     * @description 查看短信发送记录和发送状态。可以根据短信发送日期查看发送记录和短信内容，也可以添加发送流水号，根据流水号查询指定日期指定请求的发送详情。
     *
     *   如果指定日期短信发送量较大，可以分页查看。指定每页显示的短信详情数量和查看的页数，即可分页查看发送记录。
     */
    public static Map<String,Object> querySendDetails(String phoneNumber,String sendDate,String pageSize,String currentPage) throws Exception
    {
        Client client = getClient();
        QuerySendDetailsRequest querySendDetailsRequest = new QuerySendDetailsRequest()
                .setPhoneNumber(phoneNumber)
                .setSendDate(sendDate)
                .setPageSize(Long.parseLong(pageSize))
                .setCurrentPage(Long.parseLong(currentPage));
        // 复制代码运行请自行打印 API 的返回值
        QuerySendDetailsResponse response = client.querySendDetails(querySendDetailsRequest);
        return response.getBody().getSmsSendDetailDTOs().toMap();
    }

    private static Client getClient() throws Exception
    {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(AliSMSConfig.accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(AliSMSConfig.accessSecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }
}
