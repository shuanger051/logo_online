package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SysUserStatusIO {

    /**
     * 用户名称
     */
    @NotNull(message = "用户名称不能为空")
    private String userName;

    /**
     * 是否禁用：0 不禁用 1 禁用
     */
    @DictValidator(value = "isDisabled",message = "禁用状态参数非法")
    private String isDisabled;

    @Override
    public String toString() {
        return "SysUserStatusIO{" +
                "userName='" + userName + '\'' +
                ", isDisabled='" + isDisabled + '\'' +
                '}';
    }
}
