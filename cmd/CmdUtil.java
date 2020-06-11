import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * @version: V1.0
 * @className: CmdUtil
 * @packageName: com.shanji.cmd
 * @data: 2020/6/3 20:48
 * @description:
 *      通过调用 executeCmd 方法传入对应操作系统的命令即可完成调用系统指令的操作，调用的结果按照顺序放入字符链表中返回。
 */
public class CmdUtil
{
    public static void main(String[] args) throws Exception
    {
        Scanner input = new Scanner(System.in);
        String flag = input.nextLine();
        while (!flag.equals("exit"))
        {
            List<String> strings = executeCmd(flag);
            for(String temp : strings)
            {
                System.out.println(temp);
            }
            flag = input.nextLine();
        }
    }

    public static List<String> executeCmd(String command) throws Exception
    {
        Runtime run = Runtime.getRuntime();
        ArrayList<String> result = new ArrayList<>();
        Runtime rt = Runtime.getRuntime();

        Properties props=System.getProperties();
        String name = props.getProperty("os.name");
        Process pr = null;
        if(name.toLowerCase().contains("windows"))
        {
            pr = rt.exec("cmd /c " + command);
        }
        else
        {
            String[] cmd = {"/bin/sh", "-c", command};
            pr = rt.exec(cmd);
        }
        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
        String line = null;
        while ((line = input.readLine()) != null)
        {
            if(!line.equals(""))
            {
                result.add(new String(line.getBytes(),"utf-8"));
            }
        }
        input.close();
        pr.destroy();
        return result;
    }
}
