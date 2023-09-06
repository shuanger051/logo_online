package com.qinghua.website.api.controller.vo;

import lombok.Data;

@Data
public class LogoInfoVO {

    private Long id;

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

    /**
     * 文件预览映射路径
     */
    private String urlPath;

    /**
     * 文件预览映射路径
     */
    private String compressUrlPath;

    @Override
    public String toString() {
        return "LogoInfoVO{" +
                "id=" + id +
                ", shopsId=" + shopsId +
                ", merchantId=" + merchantId +
                ", logoName='" + logoName + '\'' +
                ", logoFilePath='" + logoFilePath + '\'' +
                ", logoFileName='" + logoFileName + '\'' +
                '}';
    }

}
