package com.qinghua.website.mobile.controller.vo;

import com.qinghua.website.server.domain.ContentCheckDTO;
import com.qinghua.website.server.domain.ContentExtDTO;
import lombok.Data;

import java.util.List;

@Data
public class ContentVO {

    private Long id;

    /**
     *@Fields channel_id:栏目id
     */
    private Long channelId;

    /**
     *@Fields is_recommend:是否推荐
     */
    private String isRecommend;

    /**
     *@Fields status:状态 0:草稿;1:审核中;2:审核通过;3:回收站
     */
    private String status;

    /**
     * 日访问数
     */
    private Integer viewsDay;

    /**
     * 文章审核信息
     */
    private ContentCheckDTO contentCheck;

    /**
     * 文章扩展信息
     */
    private ContentExtDTO contentExt;

    /**
     * 文章附件信息
     */
    private List<ContentAttachmentVO> list;

    @Override
    public String toString() {
        return "ContentVO{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", isRecommend='" + isRecommend + '\'' +
                ", status='" + status + '\'' +
                ", viewsDay=" + viewsDay +
                ", contentCheckVO=" + contentCheck +
                ", contentExtVO=" + contentExt +
                ", contentAttachmentVOList=" + list +
                '}';
    }

}
