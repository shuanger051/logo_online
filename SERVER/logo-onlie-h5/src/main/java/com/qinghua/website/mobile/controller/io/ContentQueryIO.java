package com.qinghua.website.mobile.controller.io;

import com.qinghua.website.mobile.validation.DictValidator;
import lombok.Data;

@Data
public class ContentQueryIO extends BaseIO{

    /**
     *@Fields channel_id:栏目id
     */
    private Long channelId;

    /**
     *@Fields is_recommend:是否推荐
     */
    @DictValidator(value = "isRecommend",message = "是否推荐参数格式非法")
    private String isRecommend;

    /**
     *@Fields status:状态 0:草稿;1:审核中;2:审核通过;3:回收站
     */
    @DictValidator(value = "status",message = "文章状态参数格式非法")
    private String status;

    @Override
    public String toString() {
        return "ContentQueryIO{" +
                "channelId=" + channelId +
                ", isRecommend=" + isRecommend +
                ", status=" + status +
                '}';
    }

}
