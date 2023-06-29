package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

@Data
public class ChannelQueryIO extends BaseIO{

    /**
     *@Fields model_id:模型id
     */
    private Long modelId;

    /**
     *@Fields parent_id:父栏目ID
     */
    private Long parentId;

    /**
     *@Fields title:标题
     */
    private String name;

    /**
     *@Fields is_display:是否显示 1-是，0-否
     */
    @DictValidator(value = "isDisplay",message = "是否显示参数非法")
    private String isDisplay;

    /**
     *@Fields comment_control:栏目访问级别 - 0：匿名 1 会员 2 关闭
     */
    @DictValidator(value = "commentControl" ,message = "栏目访问级别参数非法")
    private String commentControl;

    /**
     *@Fields 是否开放 :1 开放 0 关闭
     */
    @DictValidator(value = "allowUpdown",message = "是否开放参数非法")
    private String allowUpdown;

    @Override
    public String toString() {
        return "ChannelQueryIO{" +
                "modelId=" + modelId +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", isDisplay='" + isDisplay + '\'' +
                ", commentControl=" + commentControl +
                ", allowUpdown=" + allowUpdown +
                '}';
    }

}
