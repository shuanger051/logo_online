package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.ContentDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 文章信息表 服务类
 * </p>
 * @author ${caijl}
 * @since 2023-06-28
 */
public interface ContentService {

        /**
        * 分页查询
        */
        PageInfo<ContentDTO> getContentListByPage(ContentDTO bean);

        /**
         * 查询List集合
         * @param bean
         * @return
         */
        List<ContentDTO> getContentList(ContentDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        ContentDTO getContentById(Long id);

        /**
         * 新增
         * @param bean
         */
        Integer saveContent(ContentDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateContentById(ContentDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteContentById(Long id);

        /**
         * 根据ChannelID查询文章集合
         * @param contentDTO
         * @return
         */
        PageInfo<ContentDTO> getContentByChannelId(ContentDTO contentDTO);

}
