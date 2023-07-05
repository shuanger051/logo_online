package com.qinghua.website.server.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ContentCheckDTO {

	private Long id;

    /**
     * 内容id
     */
	private Long contentId;

    /**
     * 审核步数
     */
	private Integer checkStep;

    /**
     * 审核意见
     */
	private String checkOpinion;

    /**
     * 是否退回
     */
	private String isRejected;

	private Date createTime;
	private Date updateTime;

	@Override
	public String toString() {
	return "ContentCheck{" +
					", id=" + this.getId() +
					", contentId=" + this.getContentId() +
					", checkStep=" + this.getCheckStep() +
					", checkOpinion=" + this.getCheckOpinion() +
					", isRejected=" + this.getIsRejected() +
			"}";
}
}