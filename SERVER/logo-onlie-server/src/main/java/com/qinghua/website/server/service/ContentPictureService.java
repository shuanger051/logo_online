package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.ContentPictureDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 文章内容图片表 服务类
 * </p>
 * @author ${caijl}
 * @since 2023-06-28
 */
public interface ContentPictureService {

        /**
        * 分页查询
        */
        PageInfo<ContentPictureDTO> getContentPictureListByPage(ContentPictureDTO bean);

        /**
         * 查询List集合
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
        ContentPictureDTO getContentPictureById(Long id);

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
        Integer deleteContentPictureById(Long id);

}
