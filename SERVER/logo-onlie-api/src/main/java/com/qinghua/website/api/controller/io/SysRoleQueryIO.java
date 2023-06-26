package com.qinghua.website.api.controller.io;

import lombok.Data;

/**
 * @Description: 角色查询入参类
 */
@Data
public class SysRoleQueryIO extends BaseIO {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色等级
     */
    private Integer roleLevel;

    /**
     * 角色状态
     */
    private String roleStatus;

    @Override
    public String toString() {
        return "SysRoleQueryReq{" +
                ", roleName='" + roleName + '\'' +
                ", roleLevel=" + roleLevel +
                ", roleStatus='" + roleStatus + '\'' +
                '}';
    }

}
