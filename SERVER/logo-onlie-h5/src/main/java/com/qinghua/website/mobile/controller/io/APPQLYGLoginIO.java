package com.qinghua.website.mobile.controller.io;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class APPQLYGLoginIO {

    /**
     * 签名 - 时间戳
     */
    @NotNull(message = "签名不能为空!")
    private String sign;

    @Override
    public String toString() {
        return "APPQLYGLoginIO{" +
                "sign='" + sign + '\'' +
                '}';
    }

}
