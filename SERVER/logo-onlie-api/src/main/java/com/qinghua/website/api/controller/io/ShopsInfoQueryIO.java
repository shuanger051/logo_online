package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ShopsInfoQueryIO {

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 营业年限，1：3年以内,2：3-5年，3:5-10年，4:10年以上，5：百年老店
     */
    @DictValidator(value = "bizYears",message = "营业年限格式非法")
    private String bizYears;

    /**
     * 行业类型
     */
    @DictValidator(value = "industryType",message = "行业类型参数格式非法")
    private String industryType;

    /**
     * 是否老店：1-是，0-不是
     */
    @DictValidator(value = "isOldShops",message = "是否老店参数格式非法")
    private String isOldShops;

    /**
     * 店铺属性：1-自有，2-租住
     */
    @DictValidator(value = "shopsType",message = "店铺属性参数格式非法")
    private String shopsType;

    /**
     * 经办人姓名
     */
    private String handledByName;

    /**
     * 经办人电话
     */
    private String handledByPhone;

    /**
     * 经办人身份证号
     */
    private String handledByIdCard;


    @Override
    public String toString() {
        return "ShopsInfoQueryIO{" +
                "address='" + address + '\'' +
                ", bizYears='" + bizYears + '\'' +
                ", industryType='" + industryType + '\'' +
                ", isOldShops='" + isOldShops + '\'' +
                ", shopsType='" + shopsType + '\'' +
                '}';
    }
}
