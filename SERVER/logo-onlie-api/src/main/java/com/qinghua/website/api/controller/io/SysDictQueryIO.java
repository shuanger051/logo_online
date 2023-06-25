package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class SysDictQueryIO extends BaseIO{

    /**
     *字典类型名称
     */
    private String dictName;

    @Override
    public String toString() {
        return "SysDictQueryIO{" +
                "dictName='" + dictName + '\'' +
                '}';
    }
}
