package com.qinghua.website.mobile.controller.io;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LogoInfoBase64IO {

    @NotNull(message = "Base64不能为空!")
    private String base64;

    private Long shopsId;

    private Long merchantId;

    @Override
    public String toString() {
        return "LogoInfoBase64IO{" +
                "base64='" + base64 + '\'' +
                ", shopsId=" + shopsId +
                ", merchantId=" + merchantId +
                '}';
    }

}
