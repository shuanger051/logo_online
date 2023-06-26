package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

/**
 * @Description: SysPermission查询入参类
 */
@Data
public class SysPermissionQueryIO extends BaseIO {

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 权限类型
     */
    @DictValidator(value = "permissionType",message = "权限类型参数非法")
    private String permissionType;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限路径
     */
    private String permissionPath;

    @Override
    public String toString() {
        return "SysPermissionQueryReq{" +
                ", parentId=" + parentId +
                ", permissionType='" + permissionType + '\'' +
                ", permissionName='" + permissionName + '\'' +
                ", permissionPath='" + permissionPath + '\'' +
                '}';
    }
}
