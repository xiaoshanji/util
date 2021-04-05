package com.shanji.aliSMS;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.JsonNode;
import com.shanji.json.JsonUtil;

import java.time.LocalDate;
import java.util.ArrayList;
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
     * @param phoneNumber 电话号码，多个用 , 分割
     * @param TemplateParam 短信模板变量对应的实际值
     * @param flag 当传入多个电话号码，是否需要逐个发送，true 是，false 否
     * @return 如果是逐条发送，会返回每个号码及其发送状态，如果不是逐条发送，直接将阿里云API返回的数据封装为map返回
     * @throws Exception
     * @description 使用同样的短信签名和模板给一个或多个电话发送短信
     */
    public static List<Map<String,String>> sendSms(String SignName, String TemplateCode, String phoneNumber, Map<String,String> TemplateParam, boolean flag) throws Exception
    {

        IAcsClient client = getClient();
        CommonRequest request = getRequest();
        List<Map<String,String>> result = new ArrayList<>();

        request.setSysAction("SendSms");
        request.putQueryParameter("SignName", SignName);
        request.putQueryParameter("TemplateCode", TemplateCode);

        // JsonUtil.objStrToJsonStr(TemplateParam) 将 map 转为 json 字符串
        request.putQueryParameter("TemplateParam", JsonUtil.objStrToJsonStr(TemplateParam));


        if(flag)
        {
            String[] arr = phoneNumber.split(",");
            for(String str : arr)
            {
                request.putQueryParameter("PhoneNumbers", str);
                CommonResponse response = client.getCommonResponse(request);

                //将阿里云API返回的json数据转为map
                Map<String, String> resultMap = JsonUtil.jsonStrToMap(response.getData());
                resultMap.put("PhoneNumbers",str);
                result.add(resultMap);
            }
            return result;
        }

        request.putQueryParameter("PhoneNumbers", phoneNumber);
        CommonResponse response = client.getCommonResponse(request);
        result.add(JsonUtil.jsonStrToMap(response.getData()));

        return result;
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
    public static Map<String,String> sendBatchSms(String[] SignNames, String TemplateCode, String[] phoneNumbers, List<Map<String,String>> TemplateParam) throws Exception
    {
        IAcsClient client = getClient();
        CommonRequest request = getRequest();

        request.setSysAction("SendBatchSms");

        request.putQueryParameter("PhoneNumberJson", JsonUtil.objStrToJsonStr(phoneNumbers));
        request.putQueryParameter("SignNameJson", JsonUtil.objStrToJsonStr(SignNames));
        request.putQueryParameter("TemplateCode", TemplateCode);
        request.putQueryParameter("TemplateParamJson", JsonUtil.objStrToJsonStr(TemplateParam));


        CommonResponse response = client.getCommonResponse(request);
        return JsonUtil.jsonStrToMap(response.getData());
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
    public static JsonNode querySendDetails(String phoneNumber,String sendDate,String pageSize,String currentPage) throws Exception
    {
        IAcsClient client = getClient();
        CommonRequest request = getRequest();

        request.setSysAction("QuerySendDetails");

        request.putQueryParameter("PhoneNumber", phoneNumber);
        request.putQueryParameter("SendDate", sendDate);
        request.putQueryParameter("PageSize", pageSize);
        request.putQueryParameter("CurrentPage", currentPage);

        CommonResponse response = client.getCommonResponse(request);
        return JsonUtil.jsonStrToJsonNode(response.getData());
    }


    private static CommonRequest getRequest()
    {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");


        //获取当前日期
        LocalDate date = LocalDate.now();


        request.setSysVersion(date.toString());
        request.putQueryParameter("RegionId", "cn-hangzhou");
        return request;
    }

    private static IAcsClient getClient()
    {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", AliSMSConfig.accessKeyId, AliSMSConfig.accessSecret);
        return new DefaultAcsClient(profile);
    }
}
