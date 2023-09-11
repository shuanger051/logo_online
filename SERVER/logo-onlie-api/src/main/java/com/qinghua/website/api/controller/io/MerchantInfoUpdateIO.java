package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import com.qinghua.website.api.validation.IdCardValidator;
import com.qinghua.website.api.validation.MobilePhone;
import com.qinghua.website.api.validation.NumValidator;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MerchantInfoUpdateIO {

    /**
     * id
     */
    private Long id;

    /**
     * 商户名称
     */
    @NotNull(message = "商户名称不能为空")
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
    @MobilePhone(message = "手机号格式错误")
    private String phone;

    /**
     * 身份证号
     */
    @IdCardValidator(message = "身份证号格式错误")
    private String idCard;

    /**
     * 宽度（米）
     */
    @NumValidator(message = "只允许两位小数的数字")
    @Max(value = 100,message = "宽度不能超过100")
    private String logoWidth;

    /**
     * 高度(米)
     */
    @NumValidator(message = "只允许最多两位小数的数字")
    @Max(value = 100,message = "高度不能超过100")
    private String logoHeight;

    /**
     * 材质：1-木质，2-石质，3-金属，4-其他
     */
    @DictValidator(value = "material",message = "材质参数格式非法")
    private String material;

    /**
     * 商铺名称
     */
    @Size(max = 50,message = "商鋪名称字数过多")
    private String shopsName;

    /**
     * 店招个数
     */
    private Long logoNum;

    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return "MerchantInfoUpdateIO{" +
                "id=" + id +
                ", merchantName='" + merchantName + '\'' +
                ", gender='" + gender + '\'' +
                ", merchantStatus='" + merchantStatus + '\'' +
                ", phone='" + phone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

}
