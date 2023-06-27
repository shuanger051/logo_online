package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.MultipleLayerValidator;
import com.qinghua.website.api.validation.MyEmail;
import com.qinghua.website.api.validation.PasswordCheck;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SysUserSaveIO {

    /**
     * user_name:用户名
     */
    @MultipleLayerValidator(notBlank = true, notBlankMessage = "用户名称不能为空",
            max = 100, lengthMessage = "用户名称不能超过100个字符")
    private String userName;

    /**
     * email:电子邮箱
     */
    @MyEmail(message = "邮箱格式不正确")
    private String email;

    /**
     * password:密码
     */
    @NotNull(message = "密码不能为空")
    @PasswordCheck(message = "密码要求最小长度8-20位，由数字、字符、字母大小写其中三种组成！")
    private String password;

    @Override
    public String toString() {
        return "SysUserSaveIO{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
