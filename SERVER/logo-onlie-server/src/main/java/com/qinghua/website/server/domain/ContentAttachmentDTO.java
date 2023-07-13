package com.qinghua.website.server.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ContentAttachmentDTO {

	private Long id;

    /**
     * 内容id
     */
	private Long contentId;

    /**
     * 排列顺序
     */
	private Integer priority;

    /**
     * 附件路径
     */
	private String attachmentPath;

    /**
     * 附件名称
     */
	private String attachmentName;

    /**
     * 文件名
     */
	private String filename;

    /**
     * 下载次数
     */
	private Integer downloadCount;

	private Date createTime;
	private Date updateTime;

	@Override
	public String toString() {
	return "ContentAttachment{" +
					", id=" + this.getId() +
					", contentId=" + this.getContentId() +
					", priority=" + this.getPriority() +
					", attachmentPath=" + this.getAttachmentPath() +
					", attachmentName=" + this.getAttachmentName() +
					", filename=" + this.getFilename() +
					", downloadCount=" + this.getDownloadCount() +
			"}";
}
}