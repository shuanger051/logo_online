package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class SysUserQueryIO extends BaseIO{

    /**
     * user_name:用户名
     */
    private String userName;

    /**
     * email:电子邮箱
     */
    private String email;

    /**
     * is_admin:是否管理员 0 不是 1 是
     */
    private String isAdmin;

    /**
     * is_disabled:是否禁用 0 不禁用 1 禁用
     */
    private String isDisabled;

    @Override
    public String toString() {
        return "SysUserIO{" +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", isDisabled='" + isDisabled + '\'' +
                '}';
    }

}
