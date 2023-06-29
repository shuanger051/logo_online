package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class LogQueryIO {

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
        return "LogQueryIO{" +
                ", ip='" + ip + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

}
