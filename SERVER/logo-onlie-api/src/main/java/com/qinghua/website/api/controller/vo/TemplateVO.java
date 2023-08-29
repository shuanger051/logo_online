package com.qinghua.website.api.controller.vo;

import lombok.Data;

@Data
public class TemplateVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 元素信息
     */
    private String domItem;

    /**
     * 发布状态：1-已发布，2-未发布
     */
    private String releaseStatus;

    /**
     * 风格类型:1-仿古，2-商务，3-休闲，4-其他
     */
    private String style;

    /**
     * 是否简单模板：1-是，0-否
     */
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
        return "TemplateVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", domItem='" + domItem + '\'' +
                ", releaseStatus='" + releaseStatus + '\'' +
                ", style='" + style + '\'' +
                '}';
    }

}
