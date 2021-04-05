
import javax.servlet.http.HttpServletRequest;

/**
 * @version: V1.0
 * @className: ProjectUtil
 * @packageName: com.shanji.project
 * @data: 2020/7/3 17:18
 * @description:  通过该工具类可以获取项目的有关信息
 */
public class ProjectUtil
{
    //获取项目完整uri
    public static String getProjectURI(HttpServletRequest request)
    {
        return request.getRequestURI();
    }

    //获取项目完整url
    public static String getProjectURL(HttpServletRequest request)
    {
        return request.getRequestURL().toString();
    }

    //获取项目名称
    public static String getProjectName(HttpServletRequest request)
    {
        return request.getContextPath().replace("/","");
    }

    //获取协议
    public static String getProjectAgreement(HttpServletRequest request)
    {
        return request.getScheme();
    }


    // 获取ip
    public static String getProjectServerIp(HttpServletRequest request)
    {
        return request.getServerName();
    }

    // 获取端口
    public static int getProjectPort(HttpServletRequest request)
    {
        return request.getServerPort();
    }

    //获取资源路径
    public static String getProjectPath(HttpServletRequest request)
    {
        return request.getServletPath();
    }
}
