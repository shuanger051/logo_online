package com.qinghua.website.api.controller.vo;

import lombok.Data;

@Data
public class CustomerInfoVO {

    private Long id;

    /**
     * 客户名称
     */
    private String customerName;

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
    private String gender;

    @Override
    public String toString() {
        return "CustomerInfoVO{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", idCard='" + idCard + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }

}
