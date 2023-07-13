package com.qinghua.website.api.controller.vo;

import lombok.Data;

@Data
public class ContentCheckVO {

    private Long id;

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * 审核步数
     */
    private Integer checkStep;

    /**
     * 审核意见
     */
    private String checkOpinion;

    /**
     * 是否退回
     */
    private Integer isRejected;

    @Override
    public String toString() {
        return "ContentCheckVO{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", checkStep=" + checkStep +
                ", checkOpinion='" + checkOpinion + '\'' +
                ", isRejected=" + isRejected +
                '}';
    }
}
