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
        * 分页查询
        */
        PageInfo<ContentAttachmentDTO> getContentAttachmentListByPage(ContentAttachmentDTO bean);

        /**
         * 查询List集合
         * @param bean
         * @return
         */
        List<ContentAttachmentDTO> getContentAttachmentList(ContentAttachmentDTO bean);

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

}
