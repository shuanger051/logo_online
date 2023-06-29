package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.ContentExtDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 文章内容扩展表 服务类
 * </p>
 * @author ${caijl}
 * @since 2023-06-28
 */
public interface ContentExtService {

        /**
        * 分页查询
        */
        PageInfo<ContentExtDTO> getContentExtListByPage(ContentExtDTO bean);

        /**
         * 查询List集合
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
        ContentExtDTO getContentExtById(Long id);

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
        Integer deleteContentExtById(Long id);

}
