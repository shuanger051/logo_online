package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.MyEmail;
import com.qinghua.website.api.validation.PasswordCheck;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SysUserUpdateIO {

    /**
     * id:
     */
    @NotNull(message = "主键ID不能为空!")
    private Long id;

    /**
     * user_name:用户名
     */
    private String userName;

    /**
     * email:电子邮箱
     */
    @MyEmail
    private String email;

    /**
     * password:密码
     */
    @PasswordCheck(message = "密码要求最小长度8-20位，由数字、字符、字母大小写其中三种组成！")
    private String password;

    /**
     * activation:激活状态
     */
    private Boolean activation;

    /**
     * activation_code:激活码
     */
    private String activationCode;

    /**
     * is_admin:是否管理员 0 不是 1 是
     */

    private String isAdmin;

    /**
     * is_disabled:0 不禁用 1 禁用
     */
    private String isDisabled;

    @Override
    public String toString() {
        return "SysUserUpdateIO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", activation=" + activation +
                ", activationCode='" + activationCode + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", isDisabled='" + isDisabled + '\'' +
                '}';
    }
}
