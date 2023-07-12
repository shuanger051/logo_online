package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TemplateSaveIO {

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空")
    private String name;

    /**
     * 元素信息
     */
    @NotNull(message = "元素信息不能为空")
    private String domItem;

    /**
     * 风格类型：1-古典风，2-现代风
     */
    @DictValidator(value = "style",message = "风格类型参数格式非法")
    private String style;

    @Override
    public String toString() {
        return "TemplateSaveIO{" +
                "name='" + name + '\'' +
                ", domItem='" + domItem + '\'' +
                ", style='" + style + '\'' +
                '}';
    }

}
