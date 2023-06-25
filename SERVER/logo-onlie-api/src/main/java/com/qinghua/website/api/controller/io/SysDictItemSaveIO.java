package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.MultipleLayerValidator;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName SysDictItemSaveReq
 * @Description 数据字典条目添加
 **/
@Data
public class SysDictItemSaveIO {

    /**
     * 数据字典KEY
     **/
    @MultipleLayerValidator(notBlank = true, notBlankMessage = "字典子项KEY不能为空",
            min = 1, max = 45, lengthMessage = "字典子项KEY长度为1-45个字符")
    private String itemKey;

    /**
     * 数据字典值
     **/
    @MultipleLayerValidator(notBlank = true, notBlankMessage = "字典子项名称不能为空",
            min = 1, max = 45, lengthMessage = "字典子项名称长度为1-45个字符")
    private String itemValue;

    /**
     * 数据字典类型key
     **/
    @NotBlank(message = "字典条目key不能为空")
    private String dictKey;

    @Override
    public String toString() {
        return "SysDictItemSaveIO{" +
                "itemKey='" + itemKey + '\'' +
                ", itemValue='" + itemValue + '\'' +
                ", dictKey='" + dictKey + '\'' +
                '}';
    }

}
