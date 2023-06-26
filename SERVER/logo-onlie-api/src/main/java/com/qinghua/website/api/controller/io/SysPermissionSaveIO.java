package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: SysPermission新增入参类
 */
@Data
public class SysPermissionSaveIO {

    /**
     * 父级ID
     */
    @NotNull(message = "父级ID不能为空")
    @Min(value = 0, message = "parentId得为自然数")
    private Long parentId;

    /**
     * 权限类型
     */
    @NotNull(message = "权限类型不能为空")
    @DictValidator(value = "permissionType",message = "权限类型参数非法")
    private String permissionType;

    /**
     * 权限分组
     */
    @NotBlank(message = "权限分组为必填属性")
    private String permissionGroup;

    /**
     * 权限名称
     */
    @NotNull(message = "权限名称不能为空")
    private String permissionName;

    /**
     * 权限路径
     */
    @NotNull(message = "权限路径不能为空")
    private String permissionPath;

    /**
     * 排序编号
     */
    private Integer sortNo;

    /**
     * 图标路径
     */
    private String iconPath;

    /**
     * 权限等级
     */
    @NotNull(message = "权限等级不能为空")
    private Integer permissionLevel;

    @Override
    public String toString() {
        return "SysPermissionSaveIO{" +
                "parentId=" + parentId +
                ", permissionType='" + permissionType + '\'' +
                ", permissionGroup='" + permissionGroup + '\'' +
                ", permissionName='" + permissionName + '\'' +
                ", permissionPath='" + permissionPath + '\'' +
                ", sortNo=" + sortNo +
                ", iconPath='" + iconPath + '\'' +
                ", permissionLevel='" + permissionLevel + '\'' +
                '}';
    }
}
