package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ContentDTO extends BaseDTO {

	private Long id;

    /**
     * 栏目id
     */
	private Long channelId;

    /**
     * 是否推荐
     */
	private String isRecommend;

    /**
     * 状态 0:草稿;1:审核中;2:审核通过;3:回收站
     */
	private String status;

    /**
     * 日访问数
     */
	private Integer viewsDay;

	/**
	 * 文章审核信息
	 */
	private ContentCheckDTO contentCheck;

	/**
	 * 文章扩展信息
	 */
	private ContentExtDTO contentExt;

	/**
	 * 文章附件信息
	 */
	private List<ContentAttachmentDTO> list;

	@Override
	public String toString() {
		return "ContentDTO{" +
				"id=" + id +
				", channelId=" + channelId +
				", isRecommend='" + isRecommend + '\'' +
				", status='" + status + '\'' +
				", viewsDay=" + viewsDay +
				", contentCheck=" + contentCheck +
				", contentExt=" + contentExt +
				", contentAttachmentList=" + list +
				'}';
	}

}