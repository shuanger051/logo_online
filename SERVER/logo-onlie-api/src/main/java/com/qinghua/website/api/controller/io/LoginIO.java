package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginIO {

    /**
     * 账户
     */
    @NotNull(message = "账户不能填空!")
    private String userName;

    /**
     * 密码
     */
    @NotNull(message = "密码不能填空!")
    private String password;

    /**
     * 验证码
     */
    @NotNull(message = "验证码字段不能为空!")
    private String captchaCode;

}
