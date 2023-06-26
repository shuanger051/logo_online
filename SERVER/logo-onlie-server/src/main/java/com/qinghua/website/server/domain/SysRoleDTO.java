package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

@Data
public class SysRoleDTO extends BaseDTO {

    /**
     * id:
     */
    private Long id;

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
        return "SysRoleDTO{" +
                ", roleName='" + roleName + '\'' +
                ", roleLevel=" + roleLevel +
                ", description='" + description + '\'' +
                ", roleStatus='" + roleStatus + '\'' +
                '}';
    }

}
