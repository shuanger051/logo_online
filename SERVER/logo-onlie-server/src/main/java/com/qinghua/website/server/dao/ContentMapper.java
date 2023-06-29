package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.ContentDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 文章信息表 Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-06-28
 */
@Mapper
public interface ContentMapper {

        /**
         * 查询列表集合
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
        ContentDTO getContentById(@Param("id") Long id);

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
        Integer deleteContentById(@Param("id") Long id);

        /**
         * 根据ChannelID 查询文章集合
         * @param contentDTO
         * @return
         */
        List<ContentDTO> getContentByChannelId(ContentDTO contentDTO);

}