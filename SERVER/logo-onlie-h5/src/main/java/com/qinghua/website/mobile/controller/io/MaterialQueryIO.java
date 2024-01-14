package com.qinghua.website.mobile.controller.io;

import com.qinghua.website.mobile.validation.DictValidator;
import lombok.Data;

@Data
public class MaterialQueryIO extends BaseIO{

    /**
     * 名称
     */
    private String name;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型：1-图片，2-文字
     */
    @DictValidator(value = "fileType",message = "文件类型参数格式非法")
    private String fileType;

    @Override
    public String toString() {
        return "MaterialQueryIO{" +
                "name='" + name + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }

}
