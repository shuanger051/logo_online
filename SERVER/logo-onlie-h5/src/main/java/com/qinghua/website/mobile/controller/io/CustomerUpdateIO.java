package com.qinghua.website.mobile.controller.io;

import com.qinghua.website.mobile.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CustomerUpdateIO {

    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
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
        return "CustomerUpdateIO{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", idCard='" + idCard + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }

}
