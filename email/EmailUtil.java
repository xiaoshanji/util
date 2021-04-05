
import com.sun.mail.util.MailSSLSocketFactory;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;



/**
 * @version: V1.0
 * @author: sendMail
 * @className: EmailUtil
 * @packageName: com.shanji.email
 * @description: 发送QQ邮件
 * @data: 2019-11-19
 *
 *
 * 需要的依赖
 *  邮箱依赖
 *  <dependency>
 *       <groupId>com.sun.mail</groupId>
 *       <artifactId>javax.mail</artifactId>
 *       <version>1.6.2</version>
 *  </dependency>
 *
 *
 *  需要一个开通 POP3/SMTP服务的邮箱的 账号 与 16位授权码，并且发件人只能是开通服务的账号，否则会报错
 **/

public class EmailUtil
{

    private static final String TEXT_YPE = "text/html;charset=UTF-8";

    /*public static void main(String[] args) throws Exception
    {
        File files = new File("E:\\pic\\4k壁纸1920x1080鬼刀\\20190929110913.jpg");

        while (true)
        {
            sendEmail("E:\\JAVA\\IDEAProject\\mybatisspring\\src\\main\\resource\\email.properties","1960439225@qq.com","测试","这是一个测试邮件");
        }
    }*/

    /**
     *
     * @param propertiesPath：POP3/SMTP服务的配置文件的路径
     * @param receiptEmail：接受人的邮箱
     * @param title：邮件标题
     * @param content：正文
     * @param attachment：文件
     * @throws Exception
     * @Description :用于发送带一个附件并不指定正文格式的邮件的邮件的邮件，正文的格式默认为 ""text/html;charset=UTF-8""
     */
    public static void sendEmail(String propertiesPath, String receiptEmail,String title, String content, File attachment) throws Exception
    {
        sendEmail(propertiesPath,receiptEmail,title,content,TEXT_YPE,attachment);
    }


    /**
     *
     * @param propertiesPath：POP3/SMTP服务的配置文件的路径
     * @param receiptEmail：接受人的邮箱
     * @param title：邮件标题
     * @param content：正文
     * @param attachments：文件数组
     * @throws Exception
     * @Description :用于发送带多个附件并不指定正文格式的邮件的邮件的邮件，多个附件以文件数组的形式传递，正文的格式默认为 ""text/html;charset=UTF-8""
     */
    public static void sendEmail(String propertiesPath, String receiptEmail,String title, String content, File[] attachments) throws Exception
    {
        sendEmail(propertiesPath,receiptEmail,title,content,TEXT_YPE,attachments);
    }

    /**
     *
     * @param propertiesPath：POP3/SMTP服务的配置文件的路径
     * @param receiptEmail：接受人的邮箱
     * @param title：邮件标题
     * @param content：正文
     * @throws Exception
     * @Description :用于发送不带附件并不指定正文格式的邮件的邮件，正文的格式默认为 ""text/html;charset=UTF-8""
     */
    public static void sendEmail(String propertiesPath, String receiptEmail,String title, String content) throws Exception
    {
        sendEmail(propertiesPath,receiptEmail,title,content,TEXT_YPE);
    }


    /**
     *
     * @param propertiesPath：POP3/SMTP服务的配置文件的路径
     * @param receiptEmail：接受人的邮箱
     * @param title：邮件标题
     * @param content：正文
     * @param contentType：正文格式
     * @param attachment：文件
     * @throws Exception
     * @Description :用于发送带一个附件并指定正文格式的邮件的邮件，多个附件以文件数组的形式传递
     */
    public static void sendEmail(String propertiesPath, String receiptEmail,String title, String content,String contentType, File attachment) throws Exception
    {
        File[] files = {attachment};
        sendEmail(propertiesPath,receiptEmail,title,content,contentType,files);
    }


    /**
     *
     * @param propertiesPath：POP3/SMTP服务的配置文件的路径
     * @param receiptEmail：接受人的邮箱
     * @param title：邮件标题
     * @param content：正文
     * @param contentType：正文格式
     * @throws Exception
     * @Description :用于发送不带附件并指定正文格式的邮件
     */
    public static void sendEmail(String propertiesPath, String receiptEmail,String title, String content,String contentType) throws Exception
    {
        File[] files = {};
        sendEmail(propertiesPath,receiptEmail,title,content,contentType,files);
    }

    /**
     *
     * @param propertiesPath：POP3/SMTP服务的配置文件的路径
     * @param receiptEmail：接受人的邮箱
     * @param title：邮件标题
     * @param content：正文
     * @param contentType：正文格式
     * @param attachments：文件数组
     * @throws Exception
     * @Description :用于发送带多个个附件并指定正文格式的邮件的邮件，多个附件以文件数组的形式传递
     */
    public static void sendEmail(String propertiesPath, String receiptEmail,String title, String content,String contentType, File[] attachments) throws Exception
    {
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesPath));

        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.qq.com"); //// 设置QQ邮件服务器
        prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
        prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码

        // QQ邮箱设置SSL加密
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);

        //1、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //传入发件人的姓名和授权码
                return new PasswordAuthentication(properties.getProperty("userEmail"),properties.getProperty("code"));
            }
        });

        //2、通过session获取transport对象
        Transport transport = session.getTransport();

        //3、通过transport对象邮箱用户名和授权码连接邮箱服务器
        transport.connect("smtp.qq.com",properties.getProperty("userEmail"),properties.getProperty("code"));

        //4、创建邮件,传入session对象
        MimeMessage mimeMessage = complexEmail(session,properties.getProperty("userEmail"),receiptEmail,title,content,contentType,attachments);

        //5、发送邮件
        transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());

        //6、关闭连接
        transport.close();
    }

    /**
     *
     * @param session
     * @param senderEmail
     * @param receiptEmail
     * @param title
     * @param content
     * @param contentType
     * @param attachments
     * @return
     * @throws Exception
     */
    private static MimeMessage complexEmail(Session session,String senderEmail, String receiptEmail, String title,String content,String contentType, File[] attachments) throws Exception {
        //消息的固定信息
        MimeMessage mimeMessage = new MimeMessage(session);

        //发件人
        mimeMessage.setFrom(new InternetAddress(senderEmail));
        //收件人
        mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(receiptEmail));
        //邮件标题
        mimeMessage.setSubject(title);

        //准备附件
        MimeMultipart allFile = new MimeMultipart();


        //邮件内容
        //正文文本
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(content,contentType);

        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(text);
        mimeMultipart.setSubType("related");//文本和图片内嵌成功

        //将拼装好的正文内容设置为主体
        MimeBodyPart contentText = new MimeBodyPart();
        contentText.setContent(mimeMultipart);
        allFile.addBodyPart(contentText);//正文

        //附件

        if(attachments != null && attachments.length != 0)
        {
            for(int i =0 , len = attachments.length ; i < len ; i++)
            {
                File file = attachments[i];
                MimeBodyPart appendix = new MimeBodyPart();
                appendix.setDataHandler(new DataHandler(new FileDataSource(file)));
                appendix.setFileName(file.getName());
                allFile.addBodyPart(appendix);//附件
            }
        }
        allFile.setSubType("mixed"); //正文和附件都存在邮件中，所有类型设置为mixed
        //放到Message消息中
        mimeMessage.setContent(allFile);
        mimeMessage.saveChanges();//保存修改

        return mimeMessage;
    }
}
