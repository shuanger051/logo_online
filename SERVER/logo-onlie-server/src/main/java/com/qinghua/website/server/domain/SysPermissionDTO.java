package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

/**
 * @Description: 权限实体类
 */
@Data
public class SysPermissionDTO extends BaseDTO {

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
     * 权限分组
     */
    private String permissionGroup;

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
     * 排序编号
     */
    private Integer sortNo;

    /**
     * 图标路径
     */
    private String iconPath;

    @Override
    public String toString() {
        return "SysPermissionDTO{" +
                "parentId=" + parentId +
                ", permissionType='" + permissionType + '\'' +
                ", permissionGroup='" + permissionGroup + '\'' +
                ", permissionLevel=" + permissionLevel +
                ", permissionName='" + permissionName + '\'' +
                ", permissionPath='" + permissionPath + '\'' +
                ", sortNo=" + sortNo +
                ", iconPath='" + iconPath + '\'' +
                '}';
    }

}
