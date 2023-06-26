package com.qinghua.website.api.controller.vo;

import lombok.Data;

/**
 * @Description: 角色权限VO展示类
 */
@Data
public class SysRolePermissionVO {

    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限名称
     */
    private String permissionName;

    @Override
    public String toString() {
        return "SysRolePermissionVO{" +
                "permissionId=" + permissionId +
                ", roleId=" + roleId +
                ", permissionName='" + permissionName + '\'' +
                '}';
    }

}
