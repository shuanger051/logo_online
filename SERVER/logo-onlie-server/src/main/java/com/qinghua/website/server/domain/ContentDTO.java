package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

import java.util.Date;

@Data
public class ContentDTO extends BaseDTO {

	private Long id;

    /**
     * 栏目id
     */
	private Long channelId;

    /**
     * 排序日期
     */
	private Date sortDate;

    /**
     * 固定级别
     */
	private Integer topLevel;

    /**
     * 是否推荐
     */
	private Integer isRecommend;

    /**
     * 状态 0:草稿;1:审核中;2:审核通过;3:回收站
     */
	private Integer status;

    /**
     * 日访问数
     */
	private Integer viewsDay;

    /**
     * 日评论数
     */
	private Integer commentsDay;

    /**
     * 日下载数
     */
	private Integer downloadsDay;

    /**
     * 日顶数
     */
	private Integer upsDay;

	@Override
	public String toString() {
	return "Content{" +
					", id=" + this.getId() +
					", channelId=" + this.getChannelId() +
					", sortDate=" + this.getSortDate() +
					", topLevel=" + this.getTopLevel() +
					", isRecommend=" + this.getIsRecommend() +
					", status=" + this.getStatus() +
					", viewsDay=" + this.getViewsDay() +
					", commentsDay=" + this.getCommentsDay() +
					", downloadsDay=" + this.getDownloadsDay() +
					", upsDay=" + this.getUpsDay() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
	}

}