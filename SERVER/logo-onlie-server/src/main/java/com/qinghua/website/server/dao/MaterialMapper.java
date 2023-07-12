package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.MaterialDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 素材信息表 Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-07-12
 */
@Mapper
public interface MaterialMapper {

        /**
         * 查询列表集合
         * @param bean
         * @return
         */
        List<MaterialDTO> getMaterialList(MaterialDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        MaterialDTO getMaterialById(@Param("id") Long id);

        /**
         * 新增
         * @param bean
         */
        Integer saveMaterial(MaterialDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateMaterialById(MaterialDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteMaterialById(@Param("id") Long id);

}