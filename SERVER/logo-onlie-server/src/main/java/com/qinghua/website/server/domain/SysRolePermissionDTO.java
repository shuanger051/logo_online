package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

/**
 * @Description: 角色权限关系实体类
 */
@Data
public class SysRolePermissionDTO extends BaseDTO {

    /**
     * id:
     */
    private Long id;

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
        return "SysRolePermissionDTO{" +
                "permissionId=" + permissionId +
                ", roleId=" + roleId +
                ", permissionName='" + permissionName + '\'' +
                '}';
    }
    
}
