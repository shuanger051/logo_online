package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

/**
 * 持久层
 * 系统配置表 DTO
 */
@Data
public class SysConfigDTO extends BaseDTO {

	private Long id;

    /**
     * 系统配置key
     */
	private String configKey;
    /**
     * 系统配置名称
     */
	private String configName;
    /**
     * 系统配置的值
     */
	private String configValue;

	@Override
	public String toString() {
	return "SysConfig{" +
					", id=" + this.getId() +
					", configKey=" + this.getConfigKey() +
					", configName=" + this.getConfigName() +
					", configValue=" + this.getConfigValue() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
	}

}