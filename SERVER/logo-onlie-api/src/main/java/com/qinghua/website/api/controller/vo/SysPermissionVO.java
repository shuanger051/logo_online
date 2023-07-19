package com.qinghua.website.api.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 权限信息VO
 */
@Data
public class SysPermissionVO implements Serializable {

    private static final long serialVersionUID = -2925296422869175213L;

    /**
     *主键ID
     */
    private Long id;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 权限类型
     */
    private String permissionType;

    /**
     * 权限层级
     */
    private Integer permissionLevel;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限路径
     */
    private String permissionPath;

    /**
     * 权限分组
     */
    private String permissionGroup;

    /**
     * 排序编号
     */
    private Integer sortNo;

    /**
     * 图标路径
     */
    private String iconPath;

    @Override
    public String toString() {
        return "SysPermissionVO{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", permissionType='" + permissionType + '\'' +
                ", permissionLevel=" + permissionLevel +
                ", permissionName='" + permissionName + '\'' +
                ", permissionPath='" + permissionPath + '\'' +
                ", permissionGroup='" + permissionGroup + '\'' +
                ", sortNo=" + sortNo +
                ", iconPath='" + iconPath + '\'' +
                '}';
    }

}
