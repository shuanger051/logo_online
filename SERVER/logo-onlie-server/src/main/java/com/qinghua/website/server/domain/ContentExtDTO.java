package com.qinghua.website.server.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ContentExtDTO {

	private Long id;

    /**
     * 内容id
     */
	private Long contentId;

    /**
     * 标题
     */
	private String title;

    /**
     * 副标题
     */
	private String shortTitle;

    /**
     * 作者
     */
	private String author;

    /**
     * 简介
     */
	private String description;

    /**
     * 来源
     */
	private String origin;

    /**
     * 来源链接
     */
	private String originUrl;

    /**
     * 发布时间
     */
	private Date releaseDate;

    /**
     * 标题图片
     */
	private String titleImg;

    /**
     * 内容图片
     */
	private String contentImg;

    /**
     * 文章内容
     */
	private String content;

    /**
     * 外部链接
     */
	private String externalLink;

	private Date createTime;
	private Date updateTime;

	@Override
	public String toString() {
	return "ContentExt{" +
					", id=" + this.getId() +
					", contentId=" + this.getContentId() +
					", title=" + this.getTitle() +
					", shortTitle=" + this.getShortTitle() +
					", author=" + this.getAuthor() +
					", description=" + this.getDescription() +
					", origin=" + this.getOrigin() +
					", originUrl=" + this.getOriginUrl() +
					", releaseDate=" + this.getReleaseDate() +
					", titleImg=" + this.getTitleImg() +
					", contentImg=" + this.getContentImg() +
					", content=" + this.getContent() +
					", externalLink=" + this.getExternalLink() +
			"}";
	}

}