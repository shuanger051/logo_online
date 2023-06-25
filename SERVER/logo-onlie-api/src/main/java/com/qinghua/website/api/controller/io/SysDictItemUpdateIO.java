package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysDictItemUpdateIO {

    /**
     * itemValue 字典子项值
     */
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
