package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

@Data
public class ShopsAttachmentDTO extends BaseDTO {

	private Long id;

    /**
     * 商铺ID
     */
	private Long shopsId;

    /**
     * 店铺图片地址
     */
	private String shopsPicPath;

    /**
     * 店铺租房合同地址
     */
	private String shopsContractPath;

    /**
     * 店铺图片名字
     */
	private String shopsPicName;

    /**
     * 店铺租房合同名字
     */
	private String shopsContractName;

	@Override
	public String toString() {
	return "ShopsAttachment{" +
					", id=" + this.getId() +
					", shopsId=" + this.getShopsId() +
					", shopsPicPath=" + this.getShopsPicPath() +
					", shopsContractPath=" + this.getShopsContractPath() +
					", shopsPicName=" + this.getShopsPicName() +
					", shopsContractName=" + this.getShopsContractName() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
}
}