package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

/**
 * 持久层
 * 模板信息表 DTO
 */
@Data
public class TemplateDTO extends BaseDTO {

	private Long id;

    /**
     * 名称
     */
	private String name;

    /**
     * 元素信息
     */
	private String domItem;

    /**
     * 发布状态：1-已发布，2-未发布
     */
	private String releaseStatus;

    /**
     * 风格类型：1-古典风，2-现代风
     */
	private String style;

	@Override
	public String toString() {
	return "Template{" +
					", id=" + this.getId() +
					", name=" + this.getName() +
					", domItem=" + this.getDomItem() +
					", releaseStatus=" + this.getReleaseStatus() +
					", style=" + this.getStyle() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
	}

}