package com.shanji.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @version: V1.0
 * @className: Result
 * @packageName: com.shanji.response
 * @data: 2019/11/26 16:50
 * @description: 组装状态码，信息，数据，返回给前端
 */
@Data
public class Result implements Serializable
{
    private static final long serialVersionUID = 2550986999682594319L;
    private Integer code;
    private String message;
    private Object data;
    public Result(ResultCode resultCode,Object data)
    {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }
    public Result(Integer code,String message,Object data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private static Result resultData(Integer code, String msg, Object data) {
        Result resultData = new Result(code,msg,data);
        resultData.setCode(code);
        resultData.setMessage(msg);
        resultData.setData(data);
        return resultData;
    }

    public static Result success()
    {
        return resultData(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }
    public static Result success(Object data)
    {
        return resultData(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
    public static Result failure(Integer code,String message,Object data)
    {
        return resultData(code, message, data);
    }
}
