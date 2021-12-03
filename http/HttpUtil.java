
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version: V1.0
 * @className: HttpUtil
 * @packageName: com.shanji.http
 * @data: 2020/7/31 16:21
 * @description: http 请求工具类，可以发送 get，post 请求，如果请求参数或者头部参数为 json 格式，需要使用 JsonUtil 进行转换
 *
 * 依赖：
     	<dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.7.16</version>
        </dependency>
 *
 */
public class HttpUtil
{
    public static String sendGet(String url, Map<String, String> headers) throws Exception
    {
        HttpRequest request = HttpRequest.get(url);
        if(headers == null)
        {
            headers = new HashMap<>();
        }
        for(Map.Entry<String,String> entry : headers.entrySet())
        {
            request.header(entry.getKey(),entry.getValue());
        }
        return request.execute().body();
    }

    public static String sendPost(String url, Map<String, String> headers,Map<String,String> params) throws Exception
    {
        HttpRequest request = HttpRequest.post(url);
        if(headers == null)
        {
            headers = new HashMap<>();
        }
        if(params == null)
        {
            params = new HashMap<>();
        }
        for(Map.Entry<String,String> entry : headers.entrySet())
        {
            request.header(entry.getKey(),entry.getValue());
        }

        for(Map.Entry<String,String> entry : params.entrySet())
        {
            request.body(entry.getKey(),entry.getValue());
        }
        return request.execute().body();
    }
}
