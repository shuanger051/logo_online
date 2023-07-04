package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.ContentExtDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 文章内容扩展表 Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-06-28
 */
@Mapper
public interface ContentExtMapper {

        /**
         * 查询列表集合
         * @param bean
         * @return
         */
        List<ContentExtDTO> getContentExtList(ContentExtDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        ContentExtDTO getContentExtById(@Param("id") Long id);

        /**
         * 根据contentId查询文章扩展信息
         * @param contentId
         * @return
         */
        ContentExtDTO getContentExtByContentId(Long contentId);

        /**
         * 新增
         * @param bean
         */
        Integer saveContentExt(ContentExtDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateContentExtById(ContentExtDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteContentExtById(@Param("id") Long id);

}