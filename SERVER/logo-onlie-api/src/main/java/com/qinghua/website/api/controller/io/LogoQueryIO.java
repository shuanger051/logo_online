package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class LogoQueryIO extends BaseIO{

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
