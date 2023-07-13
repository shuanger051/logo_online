package com.qinghua.website.api.controller.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ContentExtVO {

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
     * 图片类型
     */
    private String typeImg;

    /**
     * 外部链接
     */
    private String externalLink;

    @Override
    public String toString() {
        return "ContentExtVO{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", title='" + title + '\'' +
                ", shortTitle='" + shortTitle + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", origin='" + origin + '\'' +
                ", originUrl='" + originUrl + '\'' +
                ", releaseDate=" + releaseDate +
                ", titleImg='" + titleImg + '\'' +
                ", contentImg='" + contentImg + '\'' +
                ", content='" + content + '\'' +
                ", typeImg='" + typeImg + '\'' +
                ", externalLink='" + externalLink + '\'' +
                '}';
    }
}
