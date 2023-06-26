package com.qinghua.website.api.controller.vo;

import lombok.Data;

/**
 * @Description: 角色VO
 */
@Data
public class SysRoleVO {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色等级
     */
    private Integer roleLevel;

    /**
     * 描述
     */
    private String description;

    /**
     * 角色状态
     */
    private String roleStatus;

    @Override
    public String toString() {
        return "SysRoleVO{" +
                ", roleName='" + roleName + '\'' +
                ", roleLevel=" + roleLevel +
                ", description='" + description + '\'' +
                ", roleStatus='" + roleStatus + '\'' +
                '}';
    }

}
