package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.ContentAttachmentDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 文章内容附件信息表 Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-06-28
 */
@Mapper
public interface ContentAttachmentMapper {

        /**
         * 查询列表集合
         * @param bean
         * @return
         */
        List<ContentAttachmentDTO> getContentAttachmentList(ContentAttachmentDTO bean);

        /**
         * 根据附件名称查询
         * @param attachmentName
         * @return
         */
        ContentAttachmentDTO getAttachmentByAttachmentName(@Param("attachmentName") String attachmentName);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        ContentAttachmentDTO getContentAttachmentById(@Param("id") Long id);

        /**
         * 新增
         * @param bean
         */
        Integer saveContentAttachment(ContentAttachmentDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateContentAttachmentById(ContentAttachmentDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteContentAttachmentById(@Param("id") Long id);

        /**
         * 批量新增附件信息
         * @param list
         */
        void saveContentAttachmentByList(List<ContentAttachmentDTO> list);

        /**
         * 更新下载次数
         */
        void updateDownloadTimes(@Param("id") Long id);

}