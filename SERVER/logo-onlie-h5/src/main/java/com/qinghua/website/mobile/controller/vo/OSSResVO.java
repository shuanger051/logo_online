package com.qinghua.website.mobile.controller.vo;

import lombok.Data;

@Data
public class OSSResVO {

    /**
     * 文件请求路径
     */
    private String fileUrl;

    /**
     * 文件存放地址
     */
    private String filePath;

    @Override
    public String toString() {
        return "OSSResVO{" +
                "fileUrl='" + fileUrl + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
