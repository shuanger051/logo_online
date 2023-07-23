package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

@Data
public class TemplateQueryAPIIO extends BaseIO{

    /**
     * 风格类型：1-古典风，2-现代风
     */
    @DictValidator(value = "style",message = "风格类型参数格式非法")
    private String style;

    @Override
    public String toString() {
        return "TemplateQueryAPIIO{" +
                "style='" + style + '\'' +
                '}';
    }

}
