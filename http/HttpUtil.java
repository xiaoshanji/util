
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
 *  <dependency>
 *       <groupId>org.apache.httpcomponents</groupId>
 *       <artifactId>httpclient</artifactId>
 *       <version>4.5.2</version>
 *     </dependency>
 *
 */
public class HttpUtil
{
    // 发送 get 请求
    public static String sendGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception
    {
        // 创建HttpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        StringBuilder reqUrl = new StringBuilder(url);
        String result = "";
        /*
         * 设置param参数
         */
        if (params != null && params.size() > 0)
        {
            reqUrl.append("?");
            for (Map.Entry<String, String> param : params.entrySet())
            {
                reqUrl.append(param.getKey() + "=" + param.getValue() + "&");
            }
            url = reqUrl.subSequence(0, reqUrl.length() - 1).toString();
        }
        HttpGet httpGet = new HttpGet(url);
        /**
         * 设置头部
         */
        if (headers != null && headers.size() > 0)
        {
            for (Map.Entry<String, String> header : headers.entrySet())
            {
                httpGet.addHeader(header.getKey(), header.getValue());
            }
        }

        CloseableHttpResponse response = client.execute(httpGet);
        /**
         * 请求成功
         */
        if (response.getStatusLine().getStatusCode() == 200)
        {
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity,"utf-8");
        }
        else
        {
            System.out.println(response.getStatusLine().getStatusCode());
        }
        response.close();
        client.close();
        return result;
    }

    // 发送 post 请求
    public static String sendPost(String url, Map<String, String> headers, Map<String, String> params) throws Exception
    {
        CloseableHttpClient client = HttpClients.createDefault();
        String result = "";
        HttpPost httpPost = new HttpPost(url);

        /**
         * 设置头部
         */
        if (headers != null && headers.size() > 0)
        {
            for (Map.Entry<String, String> header : headers.entrySet())
            {
                httpPost.addHeader(header.getKey(), header.getValue());
            }
        }

        /**
         * 设置参数
         */
        List<NameValuePair> paramList = new ArrayList<>();
        if (params != null && params.size() > 0)
        {
            for (Map.Entry<String, String> param : params.entrySet())
            {
                paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }

        }

        // 模拟表单提交
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
        httpPost.setEntity(entity);

        CloseableHttpResponse response = client.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200)
        {
            HttpEntity hentity = response.getEntity();
            result = EntityUtils.toString(hentity, "utf-8");
        }
        else
        {
            System.out.println(response.getStatusLine().getStatusCode());
        }
        response.close();
        client.close();
        return result;
    }
}
