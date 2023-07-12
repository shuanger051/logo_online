package com.qinghua.website.server.domain;

import com.qinghua.website.server.common.BaseDTO;
import lombok.Data;

/**
 * 持久层
 * 素材信息表 DTO
 */
@Data
public class MaterialDTO extends BaseDTO {

	private Long id;

    /**
     * 名称
     */
	private String name;

    /**
     * 路径
     */
	private String filePath;

    /**
     * 文件名
     */
	private String fileName;

    /**
     * 文件类型：1-图片，2-文字
     */
	private String fileType;

	@Override
	public String toString() {
	return "Material{" +
					", id=" + this.getId() +
					", name=" + this.getName() +
					", filePath=" + this.getFilePath() +
					", fileName=" + this.getFileName() +
					", fileType=" + this.getFileType() +
					", createTime=" + this.getCreateTime() +
					", updateTime=" + this.getUpdateTime() +
			"}";
	}

}