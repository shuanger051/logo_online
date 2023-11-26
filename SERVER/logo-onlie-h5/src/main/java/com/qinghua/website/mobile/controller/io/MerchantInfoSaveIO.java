package com.qinghua.website.mobile.controller.io;

import com.qinghua.website.mobile.validation.DictValidator;
import com.qinghua.website.mobile.validation.IdCardValidator;
import com.qinghua.website.mobile.validation.MobilePhone;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MerchantInfoSaveIO {

    /**
     * 商户名称
     */
    @NotBlank(message = "商户名称不能为空")
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
    @NotNull(message = "手机号不能为空")
    @MobilePhone(message = "手机号格式错误")
    private String phone;

    /**
     * 身份证号
     */
    @NotNull(message = "身份证号不能为空")
    @IdCardValidator(message = "身份证号格式错误")
    private String idCard;

    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return "MerchantInfoSaveIO{" +
                "merchantName='" + merchantName + '\'' +
                ", gender='" + gender + '\'' +
                ", merchantStatus='" + merchantStatus + '\'' +
                ", phone='" + phone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

}
