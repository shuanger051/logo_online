package com.qinghua.website.api.controller.io;

import lombok.Data;

import java.util.List;

@Data
public class ContentSaveIO {

    /**
     *@Fields channel_id:栏目id
     */
    private Long channelId;

    /**
     *@Fields top_level:固定级别
     */
    private Integer topLevel;

    /**
     *@Fields is_recommend:是否推荐 0-否，1-是
     */
    private String isRecommend;

    /**
     * 扩展信息
     */
    private ContentExtSaveIO contentExt;

    /**
     * 审核信息
     */
    private ContentCheckSaveIO contentCheck;

    /**
     * 附件信息
     */
    private List<ContentAttachmentSaveIO> contentAttachment;

    @Override
    public String toString() {
        return "ContentSaveIO{" +
                "channelId=" + channelId +
                ", topLevel=" + topLevel +
                ", isRecommend='" + isRecommend + '\'' +
                ", contentExt=" + contentExt +
                ", contentCheck=" + contentCheck +
                ", contentAttachment=" + contentAttachment +
                '}';
    }

}
