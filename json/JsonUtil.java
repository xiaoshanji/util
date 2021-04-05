
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @version: V1.0
 * @className: JsonUtil
 * @packageName: com.shanji.json
 * @data: 2020/7/18 19:36
 * @description:  json工具类，可以将json字符串与各种对象转换，在调用 jsonStrToEntity时，由于泛型的限制，所以在调用以后需要进行强制转换
 *      依赖：
 *      <dependency>
 *       <groupId>com.fasterxml.jackson.core</groupId>
 *       <artifactId>jackson-databind</artifactId>
 *       <version>2.9.8</version>
 *     </dependency>
 */
public class JsonUtil
{
    private static ObjectMapper mapper = new ObjectMapper();

    // 将 json 字符串转为 Map
    public static Map<String,String> ToMap(String jsonStr) throws Exception
    {
        return mapper.readValue(jsonStr,new TypeReference<Map<String,String>>(){});
    }

    // 将 具有键值对特征的对象转为 json 字符串
    public static String ToJsonStr(Object object) throws Exception
    {
        return mapper.writeValueAsString(object);
    }

    // 通过传入实体的类对象，可以将 json 字符串转换为对应的实体对象
    public static Object ToEntity(String jsonStr,Class cla) throws Exception
    {
        return mapper.readValue(jsonStr,cla);
    }

    // 将 json 字符串转为 JsonNode 对象
    public static JsonNode ToJsonNode(String jsonStr) throws Exception
    {
        return mapper.readTree(jsonStr);
    }



}
