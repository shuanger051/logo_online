package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.ContentPictureDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 文章内容图片表 Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-06-28
 */
@Mapper
public interface ContentPictureMapper {

        /**
         * 查询列表集合
         * @param bean
         * @return
         */
        List<ContentPictureDTO> getContentPictureList(ContentPictureDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        ContentPictureDTO getContentPictureById(@Param("id") Long id);

        /**
         * 新增
         * @param bean
         */
        Integer saveContentPicture(ContentPictureDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateContentPictureById(ContentPictureDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteContentPictureById(@Param("id") Long id);

}