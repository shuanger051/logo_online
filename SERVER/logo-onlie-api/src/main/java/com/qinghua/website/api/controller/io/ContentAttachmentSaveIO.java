package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class ContentAttachmentSaveIO {

    /**
     * 排列顺序
     */
    private Integer priority;

    /**
     * 附件路径
     */
    private String attachmentPath;

    /**
     * 附件名称
     */
    private String attachmentName;

    /**
     * 文件名
     */
    private String filename;

    private String urlPath;

    @Override
    public String toString() {
        return "ContentAttachmentSaveIO{" +
                "priority=" + priority +
                ", attachmentPath='" + attachmentPath + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }

}
