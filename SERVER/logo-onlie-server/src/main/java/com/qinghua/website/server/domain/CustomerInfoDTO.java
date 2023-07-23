package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

/**
 * 持久层
 * 客户信息表 DTO
 * @author Mybatis-Generator
 * @since 2023-07-17 - ${time}
 */
@Data
public class CustomerInfoDTO extends BaseDTO {

	private Long id;

    /**
     * 客户名称
     */
	private String customerName;

	/**
	 * 密码
	 */
	private String password;

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
	return "CustomerInfo{" +
					", id=" + this.getId() +
					", customerName=" + this.getCustomerName() +
					", mobile=" + this.getMobile() +
					", idCard=" + this.getIdCard() +
					", nickName=" + this.getNickName() +
					", headImg=" + this.getHeadImg() +
					", age=" + this.getAge() +
					", gender=" + this.getGender() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
	}

}