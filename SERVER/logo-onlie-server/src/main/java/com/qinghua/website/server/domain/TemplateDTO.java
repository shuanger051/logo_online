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
     * 风格类型:1-自主设计，2-商务正式，3-古典文艺，4-休闲创意
     */
	private String style;

	/**
	 * 是否简单模板：1-是，0-否
	 */
	private String isSimpleTpl;

	/**
	 * 1-木质，2-石质，3-金属，4-其他
	 */
	private String material;

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