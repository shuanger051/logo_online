package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class TestPageIO {

    private String auth;

    private String gatewayCode;

    private String title;

    private Integer mode;

    private String taskTag;

    private Integer page;

    private Integer size;

    private String claimantName;

    private String callNumber;

    private String content;

    private String code;

    private Integer workOrderType;

    @Override
    public String toString() {
        return "TestPageIO{" +
                "auth='" + auth + '\'' +
                ", gatewayCode='" + gatewayCode + '\'' +
                ", title='" + title + '\'' +
                ", mode=" + mode +
                ", taskTag='" + taskTag + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", claimantName='" + claimantName + '\'' +
                ", callNumber='" + callNumber + '\'' +
                ", content='" + content + '\'' +
                ", code='" + code + '\'' +
                ", workOrderType=" + workOrderType +
                '}';
    }

}
