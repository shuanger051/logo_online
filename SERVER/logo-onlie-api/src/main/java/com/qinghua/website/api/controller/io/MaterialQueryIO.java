package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

@Data
public class MaterialQueryIO extends BaseIO{

    /**
     * 名称
     */
    private String name;

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
