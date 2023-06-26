package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: SysUserRole查询入参类
 */
@Data
public class SysUserRoleSaveIO {

    /**
     * 管理员ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Override
    public String toString() {
        return "SysUserRoleQueryReq{" +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }

}
