package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description: 角色新增或修改入参类
 */
@Data
public class SysRoleUpdateStatusIO {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空")
    @Min(value = 1, message = "Id得为正整数")
    private Long id;

    /**
     * 角色状态 1-启用，2-禁用
     */
    @DictValidator(value = "roleStatus",message = "角色状态参数非法")
    private String roleStatus;

    @Override
    public String toString() {
        return "SysRoleUpdateReq{" +
                "id=" + id +
                ", roleStatus='" + roleStatus + '\'' +
                '}';
    }

}
