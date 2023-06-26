package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

/**
 * @Description: 系统用户角色关系实体类
 */
@Data
public class SysUserRoleDTO extends BaseDTO {

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
        return "SysUserRoleDTO{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
