package com.qinghua.website.mobile.controller;

import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class ExceptionController extends AbstractErrorController {

    public ExceptionController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseResult<Object> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        switch (statusCode) {
            case 404:
                log.info("404异常跳转");
                return new ResponseResult(SysConstant.SYSTEM_ERROR_404.getCode(),SysConstant.SYSTEM_ERROR_404.getMsg());
            case 403:
                log.info("403异常跳转");
                return new ResponseResult(SysConstant.SYSTEM_ERROR_403.getCode(),SysConstant.SYSTEM_ERROR_403.getMsg());
            case 500:
                log.info("500异常跳转");
                return new ResponseResult(SysConstant.SYSTEM_ERROR_500.getCode(),SysConstant.SYSTEM_ERROR_500.getMsg());
            default:
                log.info("默认异常跳转");
                return new ResponseResult(SysConstant.SYSTEM_ERROR_404.getCode(),SysConstant.SYSTEM_ERROR_404.getMsg());
        }
    }

    public String getErrorPath() {
        return "/error";
    }

}
