package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

@Data
public class ShopsAttachmentDTO extends BaseDTO {

	private Long id;

    /**
     * 商铺ID
     */
	private Long shopsId;

    /**
     * 地址
     */
	private String attachmentPath;

    /**
     * 名字
     */
	private String attachmentName;

    /**
     * 源文件名称
     */
	private String fileName;

	/**
	 * 附件类型：1-身份证，2-营业执照，3-租赁合同
	 */
	private String attachmentType;

	/**
	 * 文件预览映射路径
	 */
	private String urlPath;

	@Override
	public String toString() {
		return "ShopsAttachmentDTO{" +
				"id=" + id +
				", shopsId=" + shopsId +
				", attachmentPath='" + attachmentPath + '\'' +
				", attachmentName='" + attachmentName + '\'' +
				", fileName='" + fileName + '\'' +
				'}';
	}

}