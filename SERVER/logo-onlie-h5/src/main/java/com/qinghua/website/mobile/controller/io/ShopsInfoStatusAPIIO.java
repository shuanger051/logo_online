package com.qinghua.website.mobile.controller.io;

import com.qinghua.website.mobile.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShopsInfoStatusAPIIO {

    /**
     * 商铺ID
     */
    @NotNull(message = "商铺ID不能为空")
    private Long id;

    /**
     * 备案状态: 0-未备案，1-已提交未审核，2-已备案
     */
    @NotNull(message = "备案状态不能为空")
    @DictValidator(value = "isFilings",message = "备案状态参数非法")
    private String isFilings;

    /**
     * 审核意见
     */
    private String checkInfo;

    @Override
    public String toString() {
        return "ShopsInfoStatusIO{" +
                "shopsId=" + id +
                ", isFilings='" + isFilings + '\'' +
                '}';
    }

}
