package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RSAEncryptIO {

    @NotNull(message = "公钥不能为空!")
    public String publicKey;

    /**
     * 密码
     */
    @NotNull(message = "被加密的字符串不能为空!")
    private String param;


}
