package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TemplateUpdateIO {

    private Long id;

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
     * 风格类型：1-古典风，2-现代风,3-商务风，4-极简风，5-欧式风，6-美式风，7-原木风，8-工业风，9-田园风（支持一个模板具有多个风格，多个风格时以','分割）
     */
    private String style;

    @Override
    public String toString() {
        return "TemplateUpdateIO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", domItem='" + domItem + '\'' +
                ", style='" + style + '\'' +
                '}';
    }

}
