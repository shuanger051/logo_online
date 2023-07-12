package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

/**
 * 持久层
 */
@Data
public class LogoInfoDTO extends BaseDTO {

	private Long id;

    /**
     * 店铺ID
     */
	private Long shopsId;

    /**
     * 商户ID
     */
	private Long merchantId;

    /**
     * 名称
     */
	private String logoName;

    /**
     * 路径
     */
	private String logoFilePath;

    /**
     * 文件名
     */
	private String logoFileName;

	@Override
	public String toString() {
	return "LogoInfo{" +
					", id=" + this.getId() +
					", shopsId=" + this.getShopsId() +
					", merchantId=" + this.getMerchantId() +
					", logoName=" + this.getLogoName() +
					", logoFilePath=" + this.getLogoFilePath() +
					", logoFileName=" + this.getLogoFileName() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
	}

}