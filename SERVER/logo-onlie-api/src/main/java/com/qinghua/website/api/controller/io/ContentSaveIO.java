package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ContentSaveIO {

    /**
     *@Fields channel_id:栏目id
     */
    @NotNull(message = "栏目ID不能为空")
    private Long channelId;

    /**
     *@Fields is_recommend:是否推荐 0-否，1-是
     */
    @DictValidator(value = "isRecommend" ,message = "是否推荐参数格式非法")
    private String isRecommend;

    /**
     * 扩展信息
     */
    private ContentExtSaveIO contentExt;

    /**
     * 附件信息
     */
    private List<ContentAttachmentSaveIO> contentAttachment;

    @Override
    public String toString() {
        return "ContentSaveIO{" +
                "channelId=" + channelId +
                ", isRecommend='" + isRecommend  +
                ", contentExt=" + contentExt +
                ", contentAttachment=" + contentAttachment +
                '}';
    }

}
