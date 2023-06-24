package com.qinghua.website.api.controller.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageListVO<T> {

    private Long total;

    private List<T> list = new ArrayList<>();

}
