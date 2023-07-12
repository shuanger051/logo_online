package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class LogoInfoSaveIO {

    /**
     * 店铺ID
     */
    private Long shopsId;

    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 名称
     */
    private String logoName;

    /**
     * 路径
     */
    private String logoFilePath;

    /**
     * 文件名
     */
    private String logoFileName;

    @Override
    public String toString() {
        return "LogoInfoSaveIO{" +
                "shopsId=" + shopsId +
                ", merchantId=" + merchantId +
                ", logoName='" + logoName + '\'' +
                ", logoFilePath='" + logoFilePath + '\'' +
                ", logoFileName='" + logoFileName + '\'' +
                '}';
    }

}
