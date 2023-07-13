package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.LogoInfoDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface LogoInfoService {

        /**
        * 分页查询
        */
        PageInfo<LogoInfoDTO> getLogoInfoListByPage(LogoInfoDTO bean);

        /**
         * 查询List集合
         * @param bean
         * @return
         */
        List<LogoInfoDTO> getLogoInfoList(LogoInfoDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        LogoInfoDTO getLogoInfoById(Long id);

        /**
         * 新增
         * @param bean
         */
        void saveLogoInfo(LogoInfoDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        void updateLogoInfoById(LogoInfoDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        void deleteLogoInfoById(Long id);

}
