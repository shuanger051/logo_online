package com.qinghua.website.api.controller.vo;

import lombok.Data;

@Data
public class SysDictVO {

    private Long id;

    /**
     *字典类型编码
     */
    private String dictKey;

    /**
     *字典类型名称
     */
    private String dictName;

    @Override
    public String toString() {
        return "SysDictVO{" +
                "id=" + id +
                ", dictKey='" + dictKey + '\'' +
                ", dictName='" + dictName + '\'' +
                '}';
    }

}
