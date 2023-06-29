package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

@Data
public class ChannelDTO extends BaseDTO {

    /**
     *@Fields id:
     */
    private Long id;

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
     *@Fields orderNo:排列顺序
     */
    private Integer orderNo;

    /**
     *@Fields is_display:是否显示
     */
    private String isDisplay;

    /**
     *@Fields description:描述符
     */
    private String description;

    /**
     *@Fields comment_control:0：匿名 1 会员 2 关闭
     */
    private String commentControl;

    /**
     *@Fields allow_updown:1 开发 0 关闭
     */
    private String allowUpdown;

    /**
     *@Fields channel_path:访问路径
     */
    private String channelPath;

    @Override
    public String toString() {
        return "ChannelDTO{" +
                "id=" + id +
                ", modelId=" + modelId +
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