package com.qinghua.website.mobile.controller.io;

import lombok.Data;

@Data
public class BaseIO {

    private Long id;
    private int pageNum = 1;
    private int pageSize = 10;
    private String sort;
    private String order;

    @Override
    public String toString() {
        return "BaseIO{" +
                "id=" + id +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", sort='" + sort + '\'' +
                ", order='" + order + '\'' +
                '}';
    }

}
