package com.qinghua.website.api.controller.vo;

import lombok.Data;

@Data
public class LogInfoVO {

    private Long id;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 描述
     */
    private String content;
    /**
     * URL
     */
    private String url;
    /**
     * IP
     */
    private String ip;
    /**
     * 用户名称
     */
    private String userName;

    @Override
    public String toString() {
        return "LogInfoVO{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", ip='" + ip + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
