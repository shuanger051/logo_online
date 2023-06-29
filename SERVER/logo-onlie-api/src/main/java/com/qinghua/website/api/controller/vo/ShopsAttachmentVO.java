package com.qinghua.website.api.controller.vo;

import lombok.Data;

@Data
public class ShopsAttachmentVO {

    /**
     * 店铺图片地址
     */
    private String shopsPicPath;

    /**
     * 店铺租房合同地址
     */
    private String shopsContractPath;

    /**
     * 店铺图片名字
     */
    private String shopsPicName;

    /**
     * 店铺租房合同名字
     */
    private String shopsContractName;

    @Override
    public String toString() {
        return "ShopsAttachmentVO{" +
                "shopsPicPath='" + shopsPicPath + '\'' +
                ", shopsContractPath='" + shopsContractPath + '\'' +
                ", shopsPicName='" + shopsPicName + '\'' +
                ", shopsContractName='" + shopsContractName + '\'' +
                '}';
    }
}
