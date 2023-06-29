package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

@Data
public class ContentPictureDTO extends BaseDTO {

	private Long id;

    /**
     * 排列顺序
     */
	private Integer priority;

	/**
	 * 内容ID
	 */
	private Long contentId;

    /**
     * 图片地址
     */
	private String imgPath;

    /**
     * 描述
     */
	private String description;

	@Override
	public String toString() {
	return "ContentPicture{" +
					", id=" + this.getId() +
					", priority=" + this.getPriority() +
					", contentId=" + this.getContentId() +
					", imgPath=" + this.getImgPath() +
					", description=" + this.getDescription() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
	}

}