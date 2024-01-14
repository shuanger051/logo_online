package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LogoInfoSaveIO {

    /**
     * 店铺ID
     */
    @NotNull(message = "店铺ID不能为空")
    private Long shopsId;

    /**
     * 商户ID
     */
    @NotNull(message = "商户ID不能为空")
    private Long merchantId;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空")
    private String logoName;

    /**
     * 路径
     */
    @NotNull(message = "名称不能为空")
    private String logoFilePath;

    /**
     * 文件名
     */
    @NotNull(message = "文件名不能为空")
    private String logoFileName;

    private String urlPath;

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
