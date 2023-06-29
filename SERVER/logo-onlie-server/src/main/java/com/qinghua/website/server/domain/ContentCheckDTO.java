package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

@Data
public class ContentCheckDTO extends BaseDTO {

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
	private Integer isRejected;

	@Override
	public String toString() {
	return "ContentCheck{" +
					", id=" + this.getId() +
					", contentId=" + this.getContentId() +
					", checkStep=" + this.getCheckStep() +
					", checkOpinion=" + this.getCheckOpinion() +
					", isRejected=" + this.getIsRejected() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
}
}