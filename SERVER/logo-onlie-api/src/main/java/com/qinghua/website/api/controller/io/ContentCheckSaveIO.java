package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class ContentCheckSaveIO {

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
        return "ContentCheckSaveIO{" +
                ", checkStep=" + checkStep +
                ", checkOpinion='" + checkOpinion + '\'' +
                ", isRejected=" + isRejected +
                '}';
    }

}
