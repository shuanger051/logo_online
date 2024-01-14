package com.qinghua.website.api.controller.io;

import com.qinghua.website.api.validation.DictValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShopsAttachmentIO {

    @NotNull(message = "商鋪ID不能为空")
    private Long shopsId;

    /**
     * 地址
     */
    @NotNull(message = "存储路径不能为空")
    private String attachmentPath;

    /**
     * 名字
     */
    @NotNull(message = "存储名称不能为空")
    private String attachmentName;

    /**
     * 源文件名称
     */
    @NotNull(message = "文件名称不能为空")
    private String fileName;

    /**
     * 附件类型：1-商铺正面照，2-营业执照，3-租赁合同
     */
    @NotNull(message = "附件类型不能为空")
    @DictValidator(value = "attachmentType",message = "附件类型参数错误")
    private String attachmentType;

    private String urlPath;

    @Override
    public String toString() {
        return "ShopsAttachmentIO{" +
                ", attachmentPath='" + attachmentPath + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

}
