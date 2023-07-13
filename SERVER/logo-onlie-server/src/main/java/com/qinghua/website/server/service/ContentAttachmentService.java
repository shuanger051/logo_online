package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.ContentAttachmentDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 文章内容附件信息表 服务类
 * </p>
 * @author ${caijl}
 * @since 2023-06-28
 */
public interface ContentAttachmentService {

        /**
         * 查询List集合
         * @param bean
         * @return
         */
        List<ContentAttachmentDTO> getContentAttachmentList(ContentAttachmentDTO bean);

        /**
         * 根据
         * @param attachmentName
         * @return
         */
        ContentAttachmentDTO getAttachmentByAttachmentName(String attachmentName);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        ContentAttachmentDTO getContentAttachmentById(Long id);

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
        Integer deleteContentAttachmentById(Long id);

        /**
         * 更新下载次数
         */
        void updateDownloadTimes(Long id);

        /**
         * 根据文具名称删除附件信息
         * @param attachmentName
         */
        void deleteAttachmentByName(String attachmentName);

}
