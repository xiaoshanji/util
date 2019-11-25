package com.shanji.alipay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.shanji.alipay.config.AlipayConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



/**
 * @version: V1.0
 * @author: 
 * @className: AlipayController
 * @packageName: com.shanji.alipay.controller
 * @description: 这是支付宝支付的控制器
 * @data: 2019-11-17
 *
 *
 * 需要的依赖
 *  支付宝SDK
 *  <dependency>
 *       <groupId>com.alipay.sdk</groupId>
 *       <artifactId>alipay-sdk-java</artifactId>
 *       <version>3.7.110.ALL</version>
 *  </dependency>
 *
 *  servlet：
 *  <dependency>
 *       <groupId>javax.servlet</groupId>
 *       <artifactId>javax.servlet-api</artifactId>
 *       <version>4.0.1</version>
 *       <scope>provided</scope>
 *  </dependency>
 *
 *  注意事项：
 *      1、所有的接收，返回的数据不能有乱码，统一用utf-8
 **/


@Controller
@RequestMapping("/alipay")
public class AlipayController
{

    private static AlipayClient alipayClient;
    static
    {
        if(alipayClient == null)
        {
            alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        }
    }

    @RequestMapping("/webPay")
    @ResponseBody
    private String alipayWebPay(HttpServletRequest request, HttpServletResponse response) throws Exception {


        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String(request.getParameter("WIDout_trade_no"));
        //付款金额，必填
        String total_amount = new String(request.getParameter("WIDtotal_amount"));
        //订单名称，必填
        String subject = new String(request.getParameter("WIDsubject"));
        //商品描述，可空
        String body = new String(request.getParameter("WIDbody"));

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }



	//web网页支付时的查询状态
    @RequestMapping("/webQuery")
    @ResponseBody
    public String alipayWebQuery(HttpServletRequest request,HttpServletResponse response) throws Exception
    {

        //设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDTQout_trade_no"));
        //支付宝交易号
        String trade_no = new String(request.getParameter("WIDTQtrade_no"));
        //请二选一设置

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","+"\"trade_no\":\""+ trade_no +"\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();

        //输出
        return result;
    }

	//web网页支付时的退款
    @RequestMapping("/webRefund")
    @ResponseBody
    public String alipayWebRefund(HttpServletRequest request,HttpServletResponse response) throws Exception
    {

        //设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDTRout_trade_no"));
        //支付宝交易号
        String trade_no = new String(request.getParameter("WIDTRtrade_no"));
        //请二选一设置
        //需要退款的金额，该金额不能大于订单金额，必填
        String refund_amount = new String(request.getParameter("WIDTRrefund_amount"));
        //退款的原因说明
        String refund_reason = new String(request.getParameter("WIDTRrefund_reason"));
        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        String out_request_no = new String(request.getParameter("WIDTRout_request_no"));

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"trade_no\":\""+ trade_no +"\","
                + "\"refund_amount\":\""+ refund_amount +"\","
                + "\"refund_reason\":\""+ refund_reason +"\","
                + "\"out_request_no\":\""+ out_request_no +"\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();

        //输出
        return result;
    }


	//web网页支付时的退款状态查询
    @RequestMapping("/webRefundQuery")
    @ResponseBody
    public String alipayWebRefundQuery(HttpServletRequest request,HttpServletResponse response) throws Exception
    {

        //设置请求参数
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();

        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDRQout_trade_no"));
        //支付宝交易号
        String trade_no = new String(request.getParameter("WIDRQtrade_no"));
        //请二选一设置
        //请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号，必填
        String out_request_no = new String(request.getParameter("WIDRQout_request_no"));

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                +"\"trade_no\":\""+ trade_no +"\","
                +"\"out_request_no\":\""+ out_request_no +"\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();

        //输出
        return result;
    }

	//web网页支付时的交易关闭
    @RequestMapping("/webClose")
    @ResponseBody
    public String alipayWebClose(HttpServletRequest request,HttpServletResponse response) throws Exception
    {

        //设置请求参数
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDTCout_trade_no"));
        //支付宝交易号
        String trade_no = new String(request.getParameter("WIDTCtrade_no"));
        //请二选一设置

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," +"\"trade_no\":\""+ trade_no +"\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();

        //输出
        return result;
    }



	//当面付生成二维码
    @RequestMapping("/precreate")
    @ResponseBody
    public String alipayPrecreate(HttpServletRequest request,HttpServletResponse response) throws Exception {
        if (request.getParameter("outTradeNo") != null) {
            // 一定要在创建AlipayTradeService之前设置参数


            // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
            // 需保证商户系统端不能重复，建议通过数据库sequence生成，
            String outTradeNo = request.getParameter("outTradeNo");

            // (必填) 订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
            String subject = request.getParameter("subject");

            // (必填) 订单总金额，单位为元，不能超过1亿元
            // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
            String totalAmount = request.getParameter("totalAmount");

            // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
            // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
            String undiscountableAmount = request.getParameter("undiscountableAmount");


            // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
            // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
            String sellerId = "";

            // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
            String body = "购买商品2件共15.00元";

            // 商户操作员编号，添加此参数可以为商户操作员做销售统计
            String operatorId = "xiaoshanshan_1";

            // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
            String storeId = "xiaoshanshan_store_1";

            // 支付超时，定义为120分钟
            String timeoutExpress = "120m";

            AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();//创建API对应的request类
            alipayRequest.setBizContent("{" +
                    "\"out_trade_no\":\"" + outTradeNo + "\"," +//商户订单号
                    "\"total_amount\":\"" + totalAmount + "\"," +
                    "\"subject\":\"" + subject + "\"," +
                    "\"store_id\":\"" + storeId + "\"," +
                    "\"timeout_express\":\"" + timeoutExpress + "\"}");//订单允许的最晚付款时间
            AlipayTradePrecreateResponse alipayResponse = alipayClient.execute(alipayRequest);
            String result = alipayResponse.getBody();
            return result;
        }
        return "";
    }


    //异步通知请求
    @RequestMapping("/notify")
    @ResponseBody
    public String alipayNotify(HttpServletRequest request, String out_trade_no, String trade_no, String trade_status) throws AlipayApiException {
        Map<String, String> params = getParamsMap(request);
        // 验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);

        if (signVerified) {
            //处理你的业务逻辑，更细订单状态等
            System.out.println("notify:success");
            return ("success");
        } else {
            System.out.println("notify:fail");
            return ("fail");
        }
    }




    //同步通知请求
    @RequestMapping("/return")
    @ResponseBody
    public String alipayReturn(HttpServletRequest request, String out_trade_no,String trade_no,String total_amount) throws AlipayApiException {
        Map<String, String> params = getParamsMap(request);

        // 验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);

        if (signVerified) {
            System.out.println("return:success");
            return ("success");
        } else {
            System.out.println("return:fail");
            return ("fail");
        }
    }

	//解析分离回调的参数
    private Map<String, String> getParamsMap(HttpServletRequest request) {
        Map<String,String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
//                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params;
    }

}
