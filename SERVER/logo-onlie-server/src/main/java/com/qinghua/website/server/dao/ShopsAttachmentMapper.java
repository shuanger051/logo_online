package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.ShopsAttachmentDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-06-28
 */
@Mapper
public interface ShopsAttachmentMapper {

        /**
         * 查询列表集合
         * @param bean
         * @return
         */
        List<ShopsAttachmentDTO> getShopsAttachmentList(ShopsAttachmentDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        ShopsAttachmentDTO getShopsAttachmentById(@Param("id") Long id);

        /**
         * 根据店铺ID查询店铺附件信息列表
         * @param id
         * @return
         */
        List<ShopsAttachmentDTO> getShopsAttachmentByShopsId(@Param("id")Long id);

        /**
         * 新增
         * @param bean
         */
        Integer saveShopsAttachment(ShopsAttachmentDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateShopsAttachmentById(ShopsAttachmentDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteShopsAttachmentById(@Param("id") Long id);

}