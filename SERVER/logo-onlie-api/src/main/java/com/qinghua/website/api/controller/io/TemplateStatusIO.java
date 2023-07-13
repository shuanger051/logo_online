package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TemplateStatusIO {

    @NotNull(message = "ID不能为空")
    private Long id;

    @DictValidator(value = "releaseStatus",message = "发布状态参数格式非法")
    private String releaseStatus;

    @Override
    public String toString() {
        return "TemplateStatusIO{" +
                "id=" + id +
                ", releaseStatus='" + releaseStatus + '\'' +
                '}';
    }

}
