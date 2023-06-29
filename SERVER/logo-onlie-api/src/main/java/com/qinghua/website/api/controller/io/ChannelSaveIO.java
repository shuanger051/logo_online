package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ChannelSaveIO {

    /**
     *@Fields model_id:模型id
     */
    @Min(value = 1,message = "模块ID最小为1")
    private Long modelId;

    /**
     *@Fields parent_id:父栏目ID
     */
    @NotNull(message = "父栏目ID不能为空")
    private Long parentId;

    /**
     *@Fields name:名称
     */
    @NotNull(message = "栏目名称不能为空")
    private String name;

    /**
     *@Fields orderNo:排列顺序
     */
    private Integer orderNo;

    /**
     *@Fields is_display:是否显示
     */
    @DictValidator(value = "isDisplay",message = "是否显示参数非法")
    private String isDisplay;

    /**
     *@Fields description:描述符
     */
    private String description;

    /**
     *@Fields comment_control:0：匿名 1 会员 2 关闭
     */
    @DictValidator(value = "commentControl",message = "访问级别参数非法")
    private String commentControl;

    /**
     *@Fields allow_updown:1 开发 0 关闭
     */
    @DictValidator(value = "allowUpdown",message = "开放状态参数非法")
    private String allowUpdown;

    /**
     *@Fields channel_path:访问路径
     */
    private String channelPath;

    @Override
    public String toString() {
        return "ChannelSaveIO{" +
                "modelId=" + modelId +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", orderNo=" + orderNo +
                ", isDisplay='" + isDisplay + '\'' +
                ", description='" + description + '\'' +
                ", commentControl=" + commentControl +
                ", allowUpdown=" + allowUpdown +
                ", channelPath='" + channelPath + '\'' +
                '}';
    }

}
