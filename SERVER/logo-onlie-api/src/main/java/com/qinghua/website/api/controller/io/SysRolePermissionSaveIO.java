package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description: SysRolePermission新增入参类
 */
@Data
public class SysRolePermissionSaveIO {

    /**
     * 权限ID
     */
    @NotNull(message = "权限ID不能为空")
    @Min(value = 1, message = "permissionId得为正整数")
    private Long permissionId;

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    @Min(value = 1, message = "roleId得为正整数")
    private Long roleId;

    @Override
    public String toString() {
        return "SysRolePermissionSaveReq{" +
                "permissionId=" + permissionId +
                ", roleId=" + roleId +
                '}';
    }

}
