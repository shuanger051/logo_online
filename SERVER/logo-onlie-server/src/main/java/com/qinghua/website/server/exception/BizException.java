package com.qinghua.website.server.exception;

import com.qinghua.website.server.constant.IEnum;

/**
 * 业务异常类
 */
public class BizException extends GlobalException {
    public BizException() {
    }

    public BizException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }

    public BizException(Throwable cause, String errorCode, String errorMessage) {
        super(cause, errorCode, errorMessage);
    }

    public BizException(IEnum iEnum) {
        super(iEnum.getMsg(), iEnum.getCode());
    }

}
