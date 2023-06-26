package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description: SysPermission新增入参类
 */
@Data
public class SysPermissionUpdateIO {

    /**
     * 主键ID
     */
    @NotNull(message = "ID不能为空")
    @Min(value = 1, message = "Id得为正整数")
    private Long id;

    /**
     * 父级ID
     */
    @NotNull(message = "父ID不能为空")
    private Long parentId;

    /**
     * 权限类型
     */
    @DictValidator(value = "permissionType",message = "权限类型参数非法")
    private String permissionType;

    /**
     * 权限分组
     */
    private String permissionGroup;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限路径
     */
    private String permissionPath;

    /**
     * 排序编号
     */
    private Integer sortNo;

    /**
     * 图标路径
     */
    private String iconPath;

    private Integer permissionLevel;

    @Override
    public String toString() {
        return "SysPermissionUpdateReq{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", permissionType='" + permissionType + '\'' +
                ", permissionGroup='" + permissionGroup + '\'' +
                ", permissionName='" + permissionName + '\'' +
                ", permissionPath='" + permissionPath + '\'' +
                ", sortNo=" + sortNo +
                ", iconPath='" + iconPath + '\'' +
                '}';
    }
}
