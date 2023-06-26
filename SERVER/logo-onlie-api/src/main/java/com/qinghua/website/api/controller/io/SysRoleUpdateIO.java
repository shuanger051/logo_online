package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description: 角色新增或修改入参类
 */
@Data
public class SysRoleUpdateIO {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    @Min(value = 1, message = "Id得为正整数")
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

    @Override
    public String toString() {
        return "SysRoleUpdateReq{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", roleLevel=" + roleLevel +
                ", description='" + description + '\'' +
                '}';
    }

}
