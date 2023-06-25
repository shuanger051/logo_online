package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.MultipleLayerValidator;
import lombok.Data;

/**
 * @ClassName SysDictSaveReq
 * @Description 数据字典类型添加
 **/
@Data
public class SysDictSaveIO {

    /**
     * 字典条目
     **/
    @MultipleLayerValidator(notBlank = true, notBlankMessage = "字典条目不能为空",
            min = 1, max = 45, lengthMessage = "字典条目长度为1-45个字符")
    private String dictKey;
    /**
     * 字典类型名称
     **/
    @MultipleLayerValidator(notBlank = true, notBlankMessage = "字典条目名称不能为空",
            min = 1, max = 45, lengthMessage = "字典条目名称长度为1-45个字符")
    private String dictName;

    @Override
    public String toString() {
        return "SysDictSaveIO{" +
                "dictKey='" + dictKey + '\'' +
                ", dictName='" + dictName + '\'' +
                '}';
    }

}
