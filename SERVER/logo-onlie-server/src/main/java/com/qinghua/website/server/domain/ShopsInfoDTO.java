package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

import java.util.List;

@Data
public class ShopsInfoDTO extends BaseDTO {

	private Long id;

	private Long merchantId;

    /**
     * 店铺地址
     */
	private String address;

    /**
     * 营业年限，1：3年以内,2：3-5年，3:5-10年，4:10年以上，5：百年老店
     */
	private String bizYears;

    /**
     * 营业类型：1-餐饮，2-百货，3-服务，4-其他
     */
	private String industryType;

    /**
     * 是否老店：1-是，0-不是
     */
	private String isOldShops;

	/**
	 * 店铺属性：1-自有，2-租住
	 */
	private String shopsType;

    /**
     * 备注
     */
	private String remark;

	/**
	 * 经办人姓名
	 */
	private String handledByName;

	/**
	 * 经办人电话
	 */
	private String handledByPhone;

	/**
	 * 经办人身份证号
	 */
	private String handledByIdCard;

	/**
	 * 经办人照片-正面
	 */
	private String handledByPhotoFront;

	/**
	 * 经办人照片-反面
	 */
	private String handledByPhotoOpposite;

	private List<ShopsAttachmentDTO> list;

	@Override
	public String toString() {
		return "ShopsInfoDTO{" +
				"id=" + id +
				", address='" + address + '\'' +
				", bizYears='" + bizYears + '\'' +
				", industryType='" + industryType + '\'' +
				", isOldShops='" + isOldShops + '\'' +
				", shopsType='" + shopsType + '\'' +
				", remark='" + remark + '\'' +
				", list=" + list +
				'}';
	}

}