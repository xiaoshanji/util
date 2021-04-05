package com.shanji.file;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
*使用Spring  MVC的文件上传
*/

@Controller
@RequestMapping("/file")
public class FileUtil
{

    private final String UPLOAD_PATH = "/upload/image";

    //使用MultipartFile
    @RequestMapping("/uploadMultipartFile")
    @ResponseBody
    public Map uploadMultipartFile(MultipartFile file)
    {
        Map<String,Object> result = new HashMap<>();
        String fileName = file.getOriginalFilename();
        file.getContentType();
        File file2 = new File(fileName);
        try
        {
            file.transferTo(file2);
            result.put("success",true);
            result.put("msg","上传成功");
        }
        catch (Exception ex)
        {
            result.put("success",false);
            result.put("msg","上传失败");
            ex.printStackTrace();
        }
        return result;
    }


    @RequestMapping("/uploadPart")
    @ResponseBody
    public Map uploadPart(Part file)
    {
        Map<String,Object> result = new HashMap<>();
        String fileName = file.getSubmittedFileName();
        file.getContentType();
        File file2 = new File(fileName);
        try
        {
            //此时的路径是web.xml的路径加上下面的参数
            file.write(UPLOAD_PATH);
            result.put("success",true);
            result.put("msg","上传成功");
        }
        catch (Exception ex)
        {
            result.put("success",false);
            result.put("msg","上传失败");
            ex.printStackTrace();
        }
        return result;
    }


    //通过 httprequest获取单个文件
    public static MultipartFile getFile(HttpServletRequest request,String name) throws Exception
    {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        return multipartRequest.getFile(name);
    }
}
