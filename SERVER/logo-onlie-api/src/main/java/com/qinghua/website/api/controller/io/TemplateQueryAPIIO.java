package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

@Data
public class TemplateQueryAPIIO extends BaseIO{

    /**
     * 风格类型:1-仿古，2-商务，3-休闲，4-其他
     */
    private String style;

    /**
     * 1-木质，2-石质，3-金属，4-其他
     */
    private String material;

    /**
     * 是否简单模板：1-是，0-否
     */
    @DictValidator(value = "isSimpleTpl" ,message = "是否简单模板参数格式非法")
    private String isSimpleTpl;

    @Override
    public String toString() {
        return "TemplateQueryAPIIO{" +
                "style='" + style + '\'' +
                '}';
    }

}
