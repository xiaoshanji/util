
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @version: V1.0
 * @className: ExcelUtil
 * @packageName: com.shanji.excel
 * @data: 2020/3/3 17:30
 * @description:
 */
public class ExcelListener<T> extends AnalysisEventListener<T>
{
    private static final int BATCH_COUNT = 300;
    List<T> list = new ArrayList<T>(BATCH_COUNT);
    private static int count = 0;

    @Override
    public void invoke(T t, AnalysisContext analysisContext)
    {
        System.out.println("解析到一条数据:{ "+ t.toString() +" }");
        list.add(t);
        count++;
        if (list.size() >= BATCH_COUNT) {
            saveData( count );
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData( count );
        list.clear();
        System.out.println("所有数据解析完成！");
        System.out.println(" count ：" + count);
    }

    private void saveData(int count) {
        System.out.println("{ "+ count +" }条数据，开始存储数据库！" + list.size());


        /**
         * 这里进行保存数据的操作
         */
        System.out.println("存储数据库成功！");
    }
}
