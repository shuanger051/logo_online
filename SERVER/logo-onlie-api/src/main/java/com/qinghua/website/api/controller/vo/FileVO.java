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

    @Override
    public String toString() {
        return "FileVO{" +
                "fileName='" + fileName + '\'' +
                ", attachmentPath='" + attachmentPath + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                '}';
    }

}
