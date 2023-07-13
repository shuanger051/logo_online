package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class MaterialSaveIO {

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

    @Override
    public String toString() {
        return "MaterialSaveIO{" +
                "name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }

}
