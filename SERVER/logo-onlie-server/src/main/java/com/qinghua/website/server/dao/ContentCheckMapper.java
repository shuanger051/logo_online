package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.ContentCheckDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 内容审核信息表 Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-06-28
 */
@Mapper
public interface ContentCheckMapper {

        /**
         * 查询列表集合
         * @param bean
         * @return
         */
        List<ContentCheckDTO> getContentCheckList(ContentCheckDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        ContentCheckDTO getContentCheckById(@Param("id") Long id);

        /**
         * 根据ContentID 查询文章审核信息
         * @param contentId
         * @return
         */
        ContentCheckDTO getContentCheckByContentId(Long contentId);

        /**
         * 新增
         * @param bean
         */
        Integer saveContentCheck(ContentCheckDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateContentCheckById(ContentCheckDTO bean);

        /**
         * 根据ContentId更新
         * @param bean
         */
        void updateContentCheckByContentId(ContentCheckDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteContentCheckById(@Param("id") Long id);

        /**
         * 根据文章ID删除审核信息
         * @param contentId
         */
        void deleteContentCheckByContentId(@Param("contentId")Long contentId);

}