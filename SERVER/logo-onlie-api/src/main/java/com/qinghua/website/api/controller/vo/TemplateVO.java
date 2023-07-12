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
     * 风格类型：1-古典风，2-现代风
     */
    private String style;

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
