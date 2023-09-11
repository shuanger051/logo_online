package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

@Data
public class MerchantInfoDTO extends BaseDTO {

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
	 * 宽度（米）
	 */
	private Double logoWidth;

	/**
	 * 高度(米)
	 */
	private Double logoHeight;

	/**
	 * 材质：1-木质，2-石质，3-金属，4-其他
	 */
	private String material;

	/**
	 * 商铺名称
	 */
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
	return "MerchantInfo{" +
					", id=" + this.getId() +
					", merchantName=" + this.getMerchantName() +
					", gender=" + this.getGender() +
					", merchantStatus=" + this.getMerchantStatus() +
					", phone=" + this.getPhone() +
					", idCard=" + this.getIdCard() +
					", remark=" + this.getRemark() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
	}

}