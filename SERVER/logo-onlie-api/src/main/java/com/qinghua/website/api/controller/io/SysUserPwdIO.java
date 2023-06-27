package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.PasswordCheck;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SysUserPwdIO {

    /**
     * 用户名称
     */
    @NotNull(message = "用户名称不能为空")
    private String userName;

    /**
     * 旧密码
     */
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    @PasswordCheck(message = "密码要求最小长度8-20位，由数字、字符、字母大小写其中三种组成！")
    private String newPassword;

    @Override
    public String toString() {
        return "SysUserPwdIO{" +
                "userName='" + userName + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

}
