package com.qinghua.website.api.controller.io;

import lombok.Data;

@Data
public class TestLoginIO {

    private String account;

    private String pwd;

    @Override
    public String toString() {
        return "TestLoginIO{" +
                "account='" + account + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }

}
