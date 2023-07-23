package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.PasswordCheck;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CustomerPwdUpdateIO {

    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    @PasswordCheck
    private String newPassword;

    /**
     * 旧密码
     */
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;

    @Override
    public String toString() {
        return "CustomerPwdUpdateIO{" +
                "id=" + id +
                ", newPassword='" + newPassword + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                '}';
    }

}
