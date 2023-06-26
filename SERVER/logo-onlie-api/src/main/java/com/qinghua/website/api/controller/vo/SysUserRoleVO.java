package com.qinghua.website.api.controller.vo;

import lombok.Data;

/**
 * @Description: 用户角色VO类
 */
@Data
public class SysUserRoleVO {

    /**
     * id:
     */
    private Long id;

    /**
     * 系统管理员ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;


    @Override
    public String toString() {
        return "SysUserRoleVO{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }

}
