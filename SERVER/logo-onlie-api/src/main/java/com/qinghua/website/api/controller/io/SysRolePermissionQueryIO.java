package com.qinghua.website.api.controller.io;

import lombok.Data;

/**
 * @Description: SysRolePermission查询入参类
 */
@Data
public class SysRolePermissionQueryIO extends BaseIO {

    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 角色ID
     */
    private Long roleId;

    @Override
    public String toString() {
        return "SysRolePermissionQueryReq{" +
                "permissionId=" + permissionId +
                ", roleId=" + roleId +
                '}';
    }

}
