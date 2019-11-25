package com.shanji.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig
{


    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016092500593515";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCq7Bi83Anx6qKFzVJ2+RYKKRm6I9dGPDg0uKYJ3pzVCPSccuK6OPiXw00XJOwuEBMZUQ44aVw/OWpYQiGpvDr9ljXSvAI8cYRqeANAJN32ZqOc7BUwyuh2IMQXXNaPcgaczVQs83FbgQ1wpk9GbNi4rAHS/QVqloczjmRmJnfdGJmMIHPhU+kpf1PUeGrg8JLtcnKf7/9v3aLgHKu/KYKraWmNj6PfYb/OrETrIaDQl0xPBF+1MI1qOqBm7XrGHZehweSmbywqnsj3FQgtLybC3YJfSoZQH4jpYuQRKAZzIzYvet+9nPtUUwYfdDTb84Iv/oS/d/U49NQ6i0G1HkiPAgMBAAECggEAbq6aylnXGtAtVGYEW8SrEANThKVG91VxtDpKUyPT/WRiOplu1tajrGstFaq0QpPoOkGx6rfXo52DjwmsEOUychp+Wiujaw67h9KVkEkPb0tB8hEzG7iZ1j0bnmFhE9zsCUgxlTZEFtQ4JtWbimhb4yKHv+BXTJGTlcBEpZ2Nix8bUFf4l5W6F75CMEvbjS7638vXGZJnuV5hulZHOGJm4cYM1IsowhRPRtAwX5WEVwEqqv9RjGkQyw+r9P6Kx5ssT5YB5Gj1fqVZ58abQepECt9B19vyrlIyaLK3pyhlOhMwSq08+DayeJ+p98m2YvkN5QJLRF5L5znX5H5pTP3vcQKBgQD27iXxSsElrr5XJUGD2CutmUaDX2w0StS8yTQi6r74CTJf332egi/yLfhlBoLrSnCE6sJ2x1/QQ0bow4+7cVeXMbLETZJtMqWL3aBVllJfXQUIrNBZonavkYkDF9nIy85qrv5+JXb/n0wvIaD+7AIQvT53ON3uCJoM0KWU71lgWQKBgQCxM0FgayO+PaCpNSo5Rt+R3mm5D/iDuyCDo81WiRB6uD0psfq0aSA0h7PTQD5W3SHv32YLoLPFZbty8IhwRDD1fBdkwvzKHlbXw5KQEep3WAYoqoWk/Rw45MY3wO8+Xl6SJg9n5rnEh15HYWgEhgPokk2uT0uQ1xqYhHR+g+oTJwKBgBQX+wRwldkQKDMFOBiXfa5pu6AzIqX6tOcL5G829QVQEkZU9G7ynx4w+XaXGg41h4R5aguqW7Sh1OJaD5csOPl3tNleipzSM0B3b3Wnftp9wkwba/TvUIMm76RNy44Yb5qk1NbFC7Lvbo6jSOuVnvFnxO6EtSKKlqaWT/MXqwk5AoGATqCHqdjgi1GdMHe+vJuOKJOgJdXIV+t4ifDs9DDtdwa5XP70V4kuGSD90gb3omOM43slNJOiMTzq6B2C7Q3VC5h8aOF8edLsheDp77ODbmFImlLP6l3FYBljOuoJPotxOzhYLapbU7EdTF8UYK12noQMpAPba/GGg5aCeoDeJJECgYB91JW9X2QKFG/+fFEqfMbjh9/7UO73C/LCmJFcoz6YfBRbYtRRdsfhNjpv+81F9Wv3htL248vd/5rhCtG3EP5zKGWZBVafLdrniZ2x63z32w9ALkCaB21kSV/+O9PgEO+YCHxT/Ow5bwBcwxNK1ad2mLZzjVGSBftKewKZzkljLw==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp/30tXrB+vDReDmwM4AxnkuI+g2udz0Bw82aegoZ8xbGDDRVLupgPVr/VhteKUrysOUMJpZYUgGcW7XRd0BiWJ0AIsWVEymGScjDWkC0R6S0aSTJnSP3b9EVnVh5VKGPy2txMFopPyEDEhTGAMmKnwA4xdwPKJhkFulxXfvj2eVAYljFCMm7ejUntP+ovBBCZEAg4eyTuAgXWecZw96VaHXua0kansD9Sa+cBA2FJ+z46Kif5ybFpzSVM5J0/MhMVUrJz4TVROY1JTIulspRcTvqlfcVkBXggEFUtQcatAsGWjDHZi9dALgI+WX6YYiScfhZsELGyviNGBe/0g2Y/QIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://d23973e2.ngrok.io/mybatisspring/alipay/notify.do";


    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://d23973e2.ngrok.io/mybatisspring/alipay/return.do";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    //正式网关：https://openapi.alipay.com/gateway.do
    //沙箱网关：https://openapi.alipaydev.com/gateway.do
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";



    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

