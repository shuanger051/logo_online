package com.qinghua.website.server.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qinghua.website.server.constant.IEnum;
import com.qinghua.website.server.constant.SysConstant;

import java.io.Serializable;

/**
 * Controller 返回对象封装
 * @param <T>
 */
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 7962380213425770636L;

    private String code;

    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseResult error(String msg) {
        return new ResponseResult(SysConstant.SYSTEM_ERROR_500.getCode(), msg, (Object)null);
    }

    public static ResponseResult error(String code, String msg) {
        return new ResponseResult(code, msg, (Object)null);
    }

    public static <T> ResponseResult error(String code, String msg, T data) {
        return new ResponseResult(code, msg, data);
    }

    public static <T> ResponseResult success(T data) {
        return new ResponseResult(SysConstant.SUCCESS.getCode(), SysConstant.SUCCESS.getMsg(), data);
    }

    public static ResponseResult success() {
        return new ResponseResult(SysConstant.SUCCESS.getCode(), SysConstant.SUCCESS.getMsg(), "success");
    }

    public static ResponseResult error(IEnum iEnum) {
        return new ResponseResult(iEnum.getCode(), iEnum.getMsg(), (Object)null);
    }

    public static <T> ResponseResult error(IEnum iEnum, T data) {
        return new ResponseResult(iEnum.getCode(), iEnum.getMsg(), data);
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

}
