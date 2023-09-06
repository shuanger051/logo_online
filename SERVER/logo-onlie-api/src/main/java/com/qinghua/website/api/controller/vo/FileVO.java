package com.qinghua.website.api.controller.vo;

import lombok.Data;

@Data
public class FileVO {

    /**
     * 源文件名称
     */
    private String fileName;

    /**
     * 上传后的存放路径
     */
    private String attachmentPath;

    /**
     * 上传后的文件别名
     */
    private String attachmentName;

    /**
     * 文件预览映射路径
     */
    private String urlPath;

    /**
     * 压缩图片路径
     */
    private String compressUrlPath;

    /**
     * 附件类型：1-身份证，2-营业执照，3-租赁合同
     */
    private String attachmentType;

    @Override
    public String toString() {
        return "FileVO{" +
                "fileName='" + fileName + '\'' +
                ", attachmentPath='" + attachmentPath + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                '}';
    }

}
