package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ContentCheckUpdateIO {

    /**
     * 内容id
     */
    @NotNull(message = "文章ID不能为空")
    private Long contentId;

    /**
     * 审核意见
     */
    @NotNull(message = "审核意见不能为空")
    private String checkOpinion;

    /**
     * 是否退回
     */
    @DictValidator(value = "isRejected",message = "是否退回参数格式非法")
    private String isRejected;

    @Override
    public String toString() {
        return "ContentCheckUpdateIO{" +
                "contentId=" + contentId +
                ", checkOpinion='" + checkOpinion + '\'' +
                ", isRejected='" + isRejected + '\'' +
                '}';
    }

}
