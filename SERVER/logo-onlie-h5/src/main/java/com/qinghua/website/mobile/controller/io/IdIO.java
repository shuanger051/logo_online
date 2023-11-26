package com.qinghua.website.mobile.controller.io;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class IdIO {

    @NotNull(message = "id不能为空")
    @Min(value = 1,message = "参数非法")
    private Long id;

    @Override
    public String toString() {
        return "IdIO{" +
                "id=" + id +
                '}';
    }
}
