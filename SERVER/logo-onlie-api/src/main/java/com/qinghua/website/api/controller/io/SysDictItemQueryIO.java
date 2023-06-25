package com.qinghua.website.api.controller.io;

import javax.validation.constraints.NotBlank;

/**
 * @Description: SysDictItem查询入参类
 */
public class SysDictItemQueryIO extends BaseIO{

    /**
     * itemValue 字典子项值
     */
    private String itemValue;

    /**
     * dictKey 字典项
     */
    @NotBlank(message = "字典项必传")
    private String dictKey;


    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

}
