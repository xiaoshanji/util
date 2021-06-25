
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

/**
 * @version: V1.0
 * @className: PdfUtil
 * @packageName: com.shanji.pdf
 * @data: 2021/4/5 16:47
 * @description: 将 word转为 pdf
 *         <dependency>
 *             <groupId>com.aspose</groupId>
 *             <artifactId>aspose-words</artifactId>
 *             <version>15.8.0</version>
 *         </dependency>
 *
 *         <dependency>
 *             <groupId>org.apache.pdfbox</groupId>
 *             <artifactId>pdfbox</artifactId>
 *             <version>2.0.12</version>
 *         </dependency>
 *
 *         <dependency>
 *             <groupId>e-iceblue</groupId>
 *             <artifactId>spire.pdf</artifactId>
 *             <version>3.9.6</version>
 *         </dependency>
 *
 *         <repository>
 *             <id>com.e-iceblue</id>
 *             <name>e-iceblue</name>
 *             <url>http://repo.e-iceblue.cn/repository/maven-public/</url>
 *         </repository>
 * <p>
 * mvn install:install-file -Dfile=aspose-words-15.8.0.jar -DgroupId=com.aspose -DartifactId=aspose-words -Dversion=15.8.0 -Dpackaging=jar
 */
public class OfficeUtil {
    public static boolean toPDF(String file) throws Exception {

        // 凭证
        String license =
                "<License>\n" +
                        "  <Data>\n" +
                        "    <Products>\n" +
                        "      <Product>Aspose.Total for Java</Product>\n" +
                        "      <Product>Aspose.Words for Java</Product>\n" +
                        "    </Products>\n" +
                        "    <EditionType>Enterprise</EditionType>\n" +
                        "    <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
                        "    <LicenseExpiry>20991231</LicenseExpiry>\n" +
                        "    <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
                        "  </Data>\n" +
                        "  <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
                        "</License>";
        InputStream is = new ByteArrayInputStream(license.getBytes("UTF-8"));
        License asposeLic = new License();
        asposeLic.setLicense(is);

        Document convertDoc = new Document(file);
        convertDoc.save(getName(file) + ".pdf", SaveFormat.PDF);
        is.close();
        return true;
    }


    public static boolean toWord(String pdf) throws Exception {
        PDDocument doc = PDDocument.load(new File(pdf));
        int pagenumber = doc.getNumberOfPages();
        String fileName = getName(pdf) + ".doc";
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        Writer writer = new OutputStreamWriter(fos, "UTF-8");
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);// 排序
        stripper.setStartPage(1);// 设置转换的开始页
        stripper.setEndPage(pagenumber);// 设置转换的结束页
        stripper.writeText(doc, writer);
        writer.close();
        doc.close();
        return true;
    }


    public static boolean toExcel(String pdf)
    {
        PdfDocument doc = new PdfDocument();
        doc.loadFromFile(pdf);

        //保存为Excel文档
        doc.saveToFile(getName(pdf) + ".xlsx", FileFormat.XLSX);
        doc.dispose();
        doc.close();
        return true;
    }

    public static String getName(String file)
    {
        return file.substring(0, file.indexOf("."));
    }

}
