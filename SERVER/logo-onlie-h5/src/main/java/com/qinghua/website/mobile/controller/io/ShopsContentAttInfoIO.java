package com.qinghua.website.mobile.controller.io;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShopsContentAttInfoIO {

    @NotNull(message = "Base64不能为空!")
    private String base64;

    private Long shopsId;

    private String attachmentType;

    @Override
    public String toString() {
        return "ShopsContentAttInfoIO{" +
                "base64='" + base64 + '\'' +
                ", shopsId=" + shopsId +
                ", attachmentType='" + attachmentType + '\'' +
                '}';
    }

}
