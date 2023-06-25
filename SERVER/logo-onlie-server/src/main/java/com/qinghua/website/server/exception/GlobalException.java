package com.qinghua.website.server.exception;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常
 */
public class GlobalException extends RuntimeException {

    private String errorCode;
    private String errorMessage;
    private Map<String, Object> errorProperties = new HashMap(10);

    public GlobalException() {
    }

    public GlobalException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public GlobalException(Throwable cause, String errorCode, String errorMessage) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public void putErrorProperty(String name, Object prop) {
        if (StringUtils.hasText(name) && !this.errorProperties.containsKey(name)) {
            this.errorProperties.put(name, prop);
        }

    }

    public Map<String, Object> getErrorProperties() {
        return this.errorProperties;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasText(this.errorMessage)) {
            sb.append(this.errorMessage);
        } else if (StringUtils.hasText(super.getMessage())) {
            sb.append(super.getMessage());
        }

        if (!this.errorProperties.isEmpty()) {
            sb.append("[");
            this.errorProperties.forEach((k, v) -> {
                sb.append(k).append("=").append(v).append(",");
            });
            sb.deleteCharAt(sb.lastIndexOf(",")).append("]");
        }

        return sb.toString();
    }

    public String getCode() {
        return this.errorCode;
    }

}
