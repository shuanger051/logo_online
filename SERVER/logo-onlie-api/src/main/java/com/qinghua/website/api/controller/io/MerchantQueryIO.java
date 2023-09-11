package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class MerchantQueryIO {

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 性别：0-保密，1-女，2-男
     */
    @DictValidator(value = "gender",message = "性别参数格式非法")
    private String gender;

    /**
     * 商户状态：1-注销，2-开业，3-停业，4-未开业
     */
    @DictValidator(value = "merchantStatus",message = "商户状态参数格式非法")
    private String merchantStatus;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 材质：1-木质，2-石质，3-金属，4-其他
     */
    @DictValidator(value = "material",message = "材质参数格式非法")
    private String material;

    /**
     * 商铺名称
     */
    @Size(max = 50,message = "输入字符过长")
    private String shopsName;

    @Override
    public String toString() {
        return "MerchantQueryIO{" +
                "merchantName='" + merchantName + '\'' +
                ", gender='" + gender + '\'' +
                ", merchantStatus='" + merchantStatus + '\'' +
                ", phone='" + phone + '\'' +
                ", idCard='" + idCard + '\'' +
                '}';
    }

}
