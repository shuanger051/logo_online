package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.MultipleLayerValidator;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysDictItemUpdateIO {

    @NotNull(message = "ID不能为空")
    @Min(value = 1, message = "Id得为正整数")
    private Long id;

    /**
     * itemValue 字典子项值
     */
    @MultipleLayerValidator(notBlank = true, notBlankMessage = "子项名称不能为空",
            min = 1, max = 45, lengthMessage = "子项名称长度为1-45个字符")
    private String itemValue;

    /**
     * dictKey 字典项
     */
    @NotBlank(message = "字典项必传")
    private String dictKey;

    @Override
    public String toString() {
        return "SysDictItemUpdateIO{" +
                "itemValue='" + itemValue + '\'' +
                ", dictKey='" + dictKey + '\'' +
                '}';
    }
}
