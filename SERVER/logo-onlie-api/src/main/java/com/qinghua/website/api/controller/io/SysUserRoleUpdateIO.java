package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description: SysUserRole查询入参类
 */
@Data
public class SysUserRoleUpdateIO {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    @Min(value = 1, message = "Id得为正整数")
    private Long id;

    /**
     * 管理员ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Override
    public String toString() {
        return "SysUserRoleQueryReq{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }

}
