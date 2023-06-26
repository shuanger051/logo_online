package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description: SysRolePermission新增入参类
 */
@Data
public class SysRolePermissionUpdateIO {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    @Min(value = 1, message = "Id得为正整数")
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "SysRolePermissionUpdateReq{" +
                "id=" + id +
                ", permissionId=" + permissionId +
                ", roleId=" + roleId +
                '}';
    }

}
