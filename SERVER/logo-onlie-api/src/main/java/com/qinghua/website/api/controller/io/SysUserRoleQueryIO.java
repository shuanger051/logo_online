package com.qinghua.website.api.controller.io;

import lombok.Data;

/**
 * @Description: SysUserRole查询入参类
 */
@Data
public class SysUserRoleQueryIO extends BaseIO{

    /**
     * 管理员ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;


    @Override
    public String toString() {
        return "SysUserRoleQueryReq{" +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }

}
