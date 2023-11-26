package com.qinghua.website.mobile.controller.io;

import lombok.Data;

@Data
public class LogoQueryIO extends BaseIO{

    private Long shopsId;

    /**
     * 名称
     */
    private String logoName;

    @Override
    public String toString() {
        return "LogoQueryIO{" +
                "logoName='" + logoName + '\'' +
                '}';
    }

}
