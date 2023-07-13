package com.qinghua.website.api.controller.io;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ContentExtSaveIO {

    /**
     * 标题
     */
    @NotNull(message = "标题不能为空")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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

    @Override
    public String toString() {
        return "ContentExtSaveIO{" +
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
                ", externalLink='" + externalLink + '\'' +
                '}';
    }

}
