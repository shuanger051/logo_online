package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import com.qinghua.website.api.validation.IdCardValidator;
import com.qinghua.website.api.validation.MobilePhone;
import com.qinghua.website.api.validation.PasswordCheck;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CustomerSaveIO {

    /**
     * 客户名称
     */
    @NotNull(message = "客户名称不能为空")
    private String customerName;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @PasswordCheck
    private String password;

    /**
     * 手机号
     */
    @MobilePhone(message = "手机号格式错误")
    private String mobile;

    /**
     * 身份证号
     */
    @IdCardValidator(message = "身份证号格式错误")
    private String idCard;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像：base64格式
     */
    private String headImg;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别：0-保密，1-女，2-男
     */
    @DictValidator(value = "gender",message = "性别参数格式非法")
    private String gender;

    @Override
    public String toString() {
        return "CustomerSaveIO{" +
                "customerName='" + customerName + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", idCard='" + idCard + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }

}
