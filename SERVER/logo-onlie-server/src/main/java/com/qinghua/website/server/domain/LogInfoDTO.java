package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

/**
 * 持久层
 * 业务访问日志表（60天一清除） DTO
 */
@Data
public class LogInfoDTO extends BaseDTO {

	private Long id;

    /**
     * 操作类型
     */
	private String type;

    /**
     * 描述
     */
	private String content;
    /**
     * URL
     */
	private String url;
    /**
     * IP
     */
	private String ip;
    /**
     * 用户名称
     */
	private String userName;

	@Override
	public String toString() {
	return "LogInfo{" +
					", id=" + this.getId() +
					", type=" + this.getType() +
					", content=" + this.getContent() +
					", url=" + this.getUrl() +
					", ip=" + this.getIp() +
					", userName=" + this.getUserName() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
	}

}