package com.qinghua.website.api.controller.io;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteAttachmentIO {

    @NotNull(message = "参数attachmentName不能为空!")
    private String attachmentName;

    @Override
    public String toString() {
        return "DeleteAttachmentIO{" +
                "attachmentName='" + attachmentName + '\'' +
                '}';
    }

}
