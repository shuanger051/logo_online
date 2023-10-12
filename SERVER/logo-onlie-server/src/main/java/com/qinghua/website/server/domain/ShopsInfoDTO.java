package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ShopsInfoDTO extends BaseDTO {

	private Long id;

	private Long merchantId;

	/**
	 * 商鋪名称
	 */
	private String shopName;

    /**
     * 店铺地址
     */
	private String address;

	/**
	 * 详细地址
	 */
	private String addressDetail;

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
	 * 备案状态: 0-未备案，1-已提交未审核，2-已备案,3-审核未通过
	 */
	private String isFilings;

	/**
	 * 审核信息
	 */
	private String checkInfo;

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

	/**
	 * 经办人照片-正面(压缩)
	 */
	private String handledByPhotoFrontCompress;

	/**
	 * 经办人照片-反面（压缩）
	 */
	private String handledByPhotoOppositeCompress;

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
	 * 店招个数
	 */
	private Long logoNum;

	private String logoName;

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