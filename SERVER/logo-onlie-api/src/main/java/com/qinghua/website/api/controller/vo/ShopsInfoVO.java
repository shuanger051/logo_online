package com.qinghua.website.api.controller.vo;

import lombok.Data;

import java.util.List;

@Data
public class ShopsInfoVO {

    private Long id;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 营业年限，1：3年以内,2：3-5年，3:5-10年，4:10年以上，5：百年老店
     */
    private String bizYears;

    /**
     * 行业类型
     */
    private String industryType;

    /**
     * 是否老店：1-是，0-不是
     */
    private String isOldShops;

    /**
     * 店铺属性：1-自有，2-租住
     */
    private String shopsType;

    /**
     * 备注
     */
    private String remark;

    private List<ShopsAttachmentVO> list;

    @Override
    public String toString() {
        return "ShopsInfoVO{" +
                "address='" + address + '\'' +
                ", bizYears='" + bizYears + '\'' +
                ", industryType='" + industryType + '\'' +
                ", isOldShops='" + isOldShops + '\'' +
                ", shopsType='" + shopsType + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
