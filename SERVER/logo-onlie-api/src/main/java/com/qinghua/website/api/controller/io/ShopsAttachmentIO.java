package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class ShopsAttachmentIO {

    private Long shopsId;

    /**
     * 地址
     */
    private String attachmentPath;

    /**
     * 名字
     */
    private String attachmentName;

    /**
     * 源文件名称
     */
    private String fileName;

    @Override
    public String toString() {
        return "ShopsAttachmentIO{" +
                ", attachmentPath='" + attachmentPath + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

}
