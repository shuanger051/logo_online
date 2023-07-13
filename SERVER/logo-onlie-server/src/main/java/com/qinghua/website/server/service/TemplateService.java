package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.TemplateDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TemplateService {

        /**
        * 分页查询
        */
        PageInfo<TemplateDTO> getTemplateListByPage(TemplateDTO bean);

        /**
         * 查询List集合
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
        TemplateDTO getTemplateById(Long id);

        /**
         * 新增
         * @param bean
         */
        void saveTemplate(TemplateDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        void updateTemplateById(TemplateDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        void deleteTemplateById(Long id);

        /**
         * 根据ID修改模板发布状态
         * @param templateDTO
         */
        void updateTemplateStatusById(TemplateDTO templateDTO);

}
