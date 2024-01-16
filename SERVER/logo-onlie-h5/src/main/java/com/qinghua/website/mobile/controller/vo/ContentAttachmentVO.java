package com.qinghua.website.mobile.controller.vo;

import lombok.Data;

@Data
public class ContentAttachmentVO {

    private Long id;

    private Long contentId;

    private Integer priority;

    private String attachmentPath;

    private String attachmentName;

    private String fileName;

    private String urlPath;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    @Override
    public String toString() {
        return "ContentAttachmentVO{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", priority=" + priority +
                ", attachmentPath='" + attachmentPath + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", urlPath='" + urlPath + '\'' +
                '}';
    }
}
