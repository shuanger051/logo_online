package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.TemplateDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 模板信息表 Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-07-12
 */
@Mapper
public interface TemplateMapper {

        /**
         * 查询列表集合
         * @param bean
         * @return
         */
        List<TemplateDTO> getTemplateList(TemplateDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        TemplateDTO getTemplateById(@Param("id") Long id);

        TemplateDTO getTemplateByIdAPI(@Param("id") Long id);


        /**
         * 新增
         * @param bean
         */
        Integer saveTemplate(TemplateDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateTemplateById(TemplateDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteTemplateById(@Param("id") Long id);

        /**
         * 根据ID更改模板发布状态
         * @param bean
         */
        void updateTemplateStatusById(TemplateDTO bean);

        /**
         * 分页查询模板信息列表
         * @param templateDTO
         * @return
         */
        List<TemplateDTO> queryTemplateListPageAPI(TemplateDTO templateDTO);

}