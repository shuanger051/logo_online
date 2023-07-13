package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

@Data
public class TemplateQueryIO extends BaseIO{

    /**
     * 名称
     */
    private String name;

    /**
     * 发布状态：1-已发布，2-未发布
     */
    @DictValidator(value = "releaseStatus",message = "发布状态参数格式非法")
    private String releaseStatus;

    /**
     * 风格类型：1-古典风，2-现代风
     */
    @DictValidator(value = "style",message = "风格类型参数格式非法")
    private String style;

    @Override
    public String toString() {
        return "TemplateQueryIO{" +
                "name='" + name + '\'' +
                ", releaseStatus='" + releaseStatus + '\'' +
                ", style='" + style + '\'' +
                '}';
    }

}
