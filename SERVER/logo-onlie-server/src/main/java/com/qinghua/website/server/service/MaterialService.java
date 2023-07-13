package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.MaterialDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaterialService {

        /**
        * 分页查询
        */
        PageInfo<MaterialDTO> getMaterialListByPage(MaterialDTO bean);

        /**
         * 查询List集合
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
        MaterialDTO getMaterialById(Long id);

        /**
         * 新增
         * @param bean
         */
        void saveMaterial(MaterialDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        void updateMaterialById(MaterialDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        void deleteMaterialById(Long id);

}
