package com.qinghua.website.server.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.UnexpectedException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({BizException.class})
    public ResponseResult resolveBizException(BizException e) {
        return ResponseResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseResult resolveHttpMethodException(HttpRequestMethodNotSupportedException e) {
        return ResponseResult.error("此方法不支持'" + e.getMethod() + "'请求");
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, MissingRequestHeaderException.class, ConstraintViolationException.class})
    private ResponseResult resolveParameterException(Exception e) {
        if (e instanceof BindException) {
            Map<String, String> paramsErrorMsgMap = new HashMap();
            String firstErrorMsg = null;
            Iterator var4 = ((BindException)e).getBindingResult().getFieldErrors().iterator();

            while(var4.hasNext()) {
                FieldError error = (FieldError)var4.next();
                if (error.contains(TypeMismatchException.class)) {
                    paramsErrorMsgMap.put(error.getField(), "数据类型错误");
                } else {
                    paramsErrorMsgMap.put(error.getField(), error.getDefaultMessage());
                }

                if (StringUtils.isBlank(firstErrorMsg)) {
                    firstErrorMsg = (String)paramsErrorMsgMap.getOrDefault(error.getField(), "数据类型错误");
                }
            }

            return ResponseResult.error(SysConstant.SYSTEM_ERROR_400.getCode(), "数据参数错误", paramsErrorMsgMap);
        } else if (e instanceof MissingServletRequestParameterException) {
            return ResponseResult.error(SysConstant.SYSTEM_ERROR_400, "[ 参数: " + ((MissingServletRequestParameterException)e).getParameterName() + " ] 必传");
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException)e;
            ConstraintViolation<?> violation = (ConstraintViolation)ex.getConstraintViolations().stream().findFirst().get();
            return ResponseResult.error(SysConstant.SYSTEM_ERROR_400, violation.getMessageTemplate());
        } else {
            return e instanceof MissingRequestHeaderException ? ResponseResult.error(SysConstant.SYSTEM_ERROR_401.getCode(), SysConstant.SYSTEM_ERROR_401.getMsg()) : ResponseResult.error(SysConstant.SYSTEM_ERROR_404);
        }
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    private ResponseResult resolveNotFundException(NoHandlerFoundException e) {
        return ResponseResult.error(SysConstant.SYSTEM_ERROR_404);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, URISyntaxException.class, HttpMessageNotReadableException.class})
    private ResponseResult resolveFrameworkException(Exception e) {
        String msg = "";
        if (e instanceof HttpMessageNotReadableException) {
            Throwable cause = e.getCause();
            if (cause instanceof JsonParseException) {
                try {
                    msg = "参数[" + ((JsonParseException)((JsonParseException)cause)).getProcessor().getCurrentName() + "]格式不正确";
                } catch (IOException var7) {
                    throw new BizException(var7.getMessage(),SysConstant.SYSTEM_ERROR_500.getCode());
                }
            } else {
                Object parser;
                if (cause instanceof InvalidFormatException) {
                    try {
                        parser = ((InvalidFormatException)((InvalidFormatException)cause)).getProcessor();
                        if (parser instanceof UTF8StreamJsonParser) {
                            msg = "参数[" + ((UTF8StreamJsonParser)((UTF8StreamJsonParser)parser)).getCurrentName() + "]格式不正确";
                        }
                    } catch (IOException var6) {
                        throw new BizException(var6.getStackTrace().toString(),SysConstant.SYSTEM_ERROR_500.getCode());
                    }
                } else if (cause instanceof JsonMappingException) {
                    try {
                        parser = ((JsonMappingException)((JsonMappingException)cause)).getProcessor();
                        if (parser instanceof UTF8StreamJsonParser) {
                            msg = "参数[" + ((UTF8StreamJsonParser)((UTF8StreamJsonParser)parser)).getCurrentName() + "]取值范围不正确";
                        }
                    } catch (IOException var5) {
                        throw new BizException(var5.getStackTrace().toString(), SysConstant.SYSTEM_ERROR_500.getCode());
                    }
                }
            }
        }

        return StringUtils.isNotEmpty(msg) ? ResponseResult.error(SysConstant.SYSTEM_ERROR_400, msg) : ResponseResult.error(SysConstant.SYSTEM_ERROR_400);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, NullPointerException.class, IndexOutOfBoundsException.class})
    private ResponseResult resolveSpecificException(Exception e) {
        if (e instanceof NumberFormatException) {
            return ResponseResult.error(SysConstant.SYSTEM_ERROR_400.getCode(), "参数类型错误,无法转换");
        } else if (!Objects.isNull(e.getMessage()) && !StringUtils.equalsIgnoreCase("", e.getMessage())) {
            return ResponseResult.error(SysConstant.SYSTEM_ERROR_400.getCode(), StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : SysConstant.SYSTEM_ERROR_500.getMsg());
        } else {
            return ResponseResult.error(SysConstant.SYSTEM_ERROR_400.getMsg());
        }
    }

    @ExceptionHandler({UnsupportedOperationException.class, UnexpectedException.class})
    private ResponseResult resolveUnsupportedOperationException(Exception ex) {
        return ResponseResult.error(SysConstant.SYSTEM_ERROR_400.getCode(), ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    private ResponseResult resolveExceptionDefault(Exception e) {
        if(e instanceof MultipartException){
            return ResponseResult.error(SysConstant.SYSTEM_ERROR_400.getCode(), "参数错误,缺少文件参数");
        }else{
            if(e.getMessage().contains("Subject does not have permission")){
                return ResponseResult.error(SysConstant.SYSTEM_ERROR_401.getCode(), "无访问权限");
            }else{
                return this.isDAOException(e.getMessage()) ? ResponseResult.error(SysConstant.SYSTEM_ERROR_500.getCode(), "数据访问异常") : ResponseResult.error(SysConstant.SYSTEM_ERROR_500);
            }
        }
    }

    private boolean isDAOException(String errorMsg) {
        return StringUtils.startsWithIgnoreCase(errorMsg, "org.springframework.dao");
    }

}
