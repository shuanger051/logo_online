package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ShopsInfoUpdateIO {

    private Long id;

    @NotNull(message = "商户ID不能为空")
    private Long merchantId;

    /**
     * 店铺地址
     */
    @NotNull(message = "店铺地址不能为空")
    private String address;

    /**
     * 营业年限，1：3年以内,2：3-5年，3:5-10年，4:10年以上，5：百年老店
     */
    @DictValidator(value = "bizYears",message = "营业年限参数格式非法")
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
     * 备注
     */
    private String remark;

    /**
     * 店铺附件
     */
    private List<ShopsAttachmentIO> list;

    @Override
    public String toString() {
        return "ShopsInfoUpdateIO{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", bizYears='" + bizYears + '\'' +
                ", industryType='" + industryType + '\'' +
                ", isOldShops='" + isOldShops + '\'' +
                ", shopsType='" + shopsType + '\'' +
                ", remark='" + remark + '\'' +
                ", shopsAttachmentIOList=" + list +
                '}';
    }
}
