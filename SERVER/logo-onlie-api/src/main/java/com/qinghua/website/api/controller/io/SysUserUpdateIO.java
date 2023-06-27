package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
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
     * email:电子邮箱
     */
    @MyEmail
    private String email;

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
    @DictValidator(value = "isAdmin",message = "isAdmin参数非法")
    private String isAdmin;

    /**
     * is_disabled:0 不禁用 1 禁用
     */
    @DictValidator(value = "isDisabled",message = "isAdmin参数非法")
    private String isDisabled;

    @Override
    public String toString() {
        return "SysUserUpdateIO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", activation=" + activation +
                ", activationCode='" + activationCode + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", isDisabled='" + isDisabled + '\'' +
                '}';
    }
}
