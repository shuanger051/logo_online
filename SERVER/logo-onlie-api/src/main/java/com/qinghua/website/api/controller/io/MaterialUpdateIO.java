package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class MaterialUpdateIO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 路径
     */
    private String filePath;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型：1-图片，2-文字
     */
    private String fileType;

    private String urlPath;

    @Override
    public String toString() {
        return "MaterialUpdateIO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }

}
