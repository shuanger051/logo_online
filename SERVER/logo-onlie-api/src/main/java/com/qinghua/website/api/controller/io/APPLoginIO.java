package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class APPLoginIO {

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

    @Override
    public String toString() {
        return "APPLoginIO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
