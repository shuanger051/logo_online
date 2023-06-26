package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.MultipleLayerValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 角色新增或修改入参类
 */
@Data
public class SysRoleSaveIO {

    /**
     * 角色名称
     */
    @MultipleLayerValidator(notBlank = true, notBlankMessage = "角色名称不能为空",
            max = 128, lengthMessage = "输入角色名称长度不能超过128个字符")
    private String roleName;

    /**
     * 角色等级
     */
    @NotNull(message = "角色等级不能为空")
    private Integer roleLevel;

    /**
     * 描述
     */
    private String description;

    @Override
    public String toString() {
        return "SysRoleSaveReq{" +
                ", roleName='" + roleName + '\'' +
                ", roleLevel=" + roleLevel +
                ", description='" + description + '\'' +
                '}';
    }

}
