package com.qinghua.website.api.controller.vo;

import lombok.Data;

@Data
public class ShopsAttachmentVO {

    private Long id;

    /**
     * 商铺ID
     */
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
        return "ShopsAttachmentVO{" +
                "id=" + id +
                ", shopsId=" + shopsId +
                ", attachmentPath='" + attachmentPath + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

}
