
import com.fasterxml.jackson.databind.JsonNode;
import com.shanji.http.HttpUtil;
import com.shanji.json.JsonUtil;


/**
 * @version: V1.0
 * @className: GaoDeMap
 * @packageName: com.shanji.over.example
 * @data: 2019/12/1 12:21
 * @description: 通过高德地图的API来转换
 *      需要环境：
 *          1、一个有WEB服务API权限的高德地图apikey
 *          2、工具类中的 HttpUtil 和 JsonUtil
 */
public class GaoDeMap
{


    //高德地图apikey
    private final static String KEY = "d7960fb02bf1049bd4d0f1f1448e0d0f";
    //将地址转为经纬度的请求url
    private static String ADDRESS_TO_SOME_URL = "http://restapi.amap.com/v3/geocode/geo?key=" + KEY +"&address=";
    //将经纬度转为地址的请求url
    private static String LAT_LOG_TO_ADDRESS_URL = "http://restapi.amap.com/v3/geocode/regeo?key=" + KEY + "&location=";

    /**
     *
     * @param address：地址
     * @return 经纬度中间用','隔开 经度在前，维度在后
     */
    public static String addressToLaLo(String address) throws Exception
    {
        //"http://restapi.amap.com/v3/geocode/geo?address=上海市东方明珠&output=JSON&key=xxxxxxxxx";
        String geturl = ADDRESS_TO_SOME_URL + address;

        String str = HttpUtil.sendGet(geturl, null, null);

        JsonNode resultNode = JsonUtil.jsonStrToJsonNode(str);
        if(resultNode.get("infocode").asText().equals("10000"))
        {
            JsonNode geocodes = resultNode.get("geocodes");
            return geocodes.get(0).get("location").asText();
        }
        return null;
    }

    /**
     *
     * @param longitude 经度
     * @param dimension 维度
     * @return  详细地址
     */
    public static String LaLoToAddress(String longitude,String dimension) throws Exception
    {
        String geturl = LAT_LOG_TO_ADDRESS_URL + longitude + "," + dimension;
        String str = HttpUtil.sendGet(geturl,null,null);

        JsonNode resultNode = JsonUtil.jsonStrToJsonNode(str);

        if(resultNode.get("infocode").asText().equals("10000"))
        {
            JsonNode regeocode = resultNode.get("regeocode");
            return regeocode.get("formatted_address").asText();
        }
        return null;
    }

    /*public static void main(String[] args) throws Exception
    {
        System.out.println(LaLoToAddress("116.481488","39.990464"));
        System.out.println(addressToLaLo("北京市朝阳区望京街道方恒国际中心B座"));
    }*/
}
