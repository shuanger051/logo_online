package com.qinghua.website.api.controller.vo;

import com.qinghua.website.server.utils.Sm4Utils;
import lombok.Data;

@Data
public class MerchantInfoVO {

    private Long id;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 性别：0-保密，1-女，2-男
     */
    private String gender;

    /**
     * 商户状态：1-注销，2-开业，3-停业，4-未开业
     */
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
     * 备注
     */
    private String remark;

    public String getPhone() {
        return Sm4Utils.decrypt(phone);
    }

    public String getIdCard() {
        return Sm4Utils.decrypt(idCard);
    }

    @Override
    public String toString() {
        return "MerchantInfoVO{" +
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
