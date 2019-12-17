package com.shanji.response;

/**
 * @version: V1.0
 * @className: ResultCode
 * @packageName: com.shanji.response.entity
 * @data: 2019/11/26 16:38
 * @description: 状态码枚举类
 */
public enum  ResultCode
{
    SUCCESS(1000,"成功"),
    PARAM_IS_INVALID(1001,"参数无效"),
    PARAM_IS_BLACK(1002,"参数为空"),
    PARAM_TYPE_BIND_ERROR(1003,"参数类型错误"),
    PARAM_NOT_COMPLETE(1004,"参数缺失"),
    USER_NOT_LOGGED_IN(2001,"用户未登录，访问的路径需要验证，请登录"),
    USER_LOGIN_ERROR(2002,"账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(2003,"账号被禁用"),
    USER_HAS_EXISTED(2005,"用户已存在");

    private Integer code;
    private String message;
    ResultCode(Integer code,String message)
    {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
