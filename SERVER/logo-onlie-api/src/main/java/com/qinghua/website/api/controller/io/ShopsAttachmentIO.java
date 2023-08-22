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

    /**
     * 附件类型：1-商铺正面照，2-营业执照，3-租赁合同
     */
    private String attachmentType;

    @Override
    public String toString() {
        return "ShopsAttachmentIO{" +
                ", attachmentPath='" + attachmentPath + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

}
