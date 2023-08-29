package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import com.qinghua.website.api.validation.IsJson;
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
    @IsJson(message = "元素信息必须为合法的JSON字符串")
    private String domItem;

    /**
     * 风格类型:1-自主设计，2-商务正式，3-古典文艺，4-休闲创意
     */
    private String style;

    /**
     * 是否简单模板：1-是，0-否
     */
    @DictValidator(value = "isSimpleTpl" ,message = "是否简单模板参数格式非法")
    private String isSimpleTpl;

    /**
     * 1-木质，2-石质，3-金属，4-其他
     */
    private String material;

    /**
     * 是否置顶：0-否，1-是
     */
    private String isTop;

    /**
     * 排序编号
     */
    private Long sortNo;

    @Override
    public String toString() {
        return "TemplateSaveIO{" +
                "name='" + name + '\'' +
                ", domItem='" + domItem + '\'' +
                ", style='" + style + '\'' +
                '}';
    }

}
