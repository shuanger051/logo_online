package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 重置密码
 */
@Data
public class SysUserUpdatePWDIO{

    /**
     * 用户名称
     */
    @NotNull(message = "用户名称不能为空")
    private String userName;

    @Override
    public String toString() {
        return "SysUserUpdatePWDIO{" +
                "userName='" + userName + '\'' +
                '}';
    }

}
