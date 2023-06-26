package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.MultipleLayerValidator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName SysDictUpdateReq
 * @Description 数据字典类型修改
 **/
public class SysDictUpdateIO {

    @NotNull(message = "ID不能为空")
    @Min(value = 1, message = "Id得为正整数")
    private Long id;
    /**
     * 字典类型名称
     **/
    @MultipleLayerValidator(notBlank = true, notBlankMessage = "字典类型名称不能为空",
            min = 1, max = 45, lengthMessage = "字典条目名称长度为1-45个字符")
    private String dictName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }
}
