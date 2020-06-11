
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;

import java.io.*;
import java.util.List;

/**
 * @version: V1.0
 * @className: ExcelUtil
 * @packageName: com.shanji.excel
 * @data: 2020/3/3 17:51
 * @description:
 */

/**
 *  上传下载表格
 *  依赖：
 *      <dependency>
 *       <groupId>com.alibaba</groupId>
 *       <artifactId>easyexcel</artifactId>
 *       <version>2.1.6</version>
 *     </dependency>
 *
 *   下载时，需要一次性将数据加载进来，分批添加数据，暂时未解决
 *
 */

public class ExcelUtil
{
    public static void readExcel(String ExcelName,Class cls) throws FileNotFoundException
    {
        readExcel(new File(ExcelName),cls);
    }
    public static void readExcel(File file, Class cls) throws FileNotFoundException
    {
        FileInputStream stream = new FileInputStream(file);
        readExcel(stream,cls);
    }
    public static void readExcel(InputStream stream, Class cls)
    {
        EasyExcel.read(stream, cls, new ExcelListener()).doReadAll();
    }

    public static void downExcel(OutputStream stream, List datas, Class cls)
    {
        EasyExcel.write(stream,cls).sheet().doWrite(datas);
    }
    public static void downExcel(String filePath, List datas, Class cls) throws FileNotFoundException {
        downExcel(new File(filePath),datas,cls);
    }
    public static void downExcel(File file, List datas, Class cls) throws FileNotFoundException {
        downExcel(new FileOutputStream(file),datas,cls);
    }
}
