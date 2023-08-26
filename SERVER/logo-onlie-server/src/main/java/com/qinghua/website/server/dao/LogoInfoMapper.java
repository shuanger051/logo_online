package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.LogoInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper 接口
 * @author Mybatis-Generator
 * @since 2023-07-12
 */
@Mapper
public interface LogoInfoMapper {

        /**
         * 查询列表集合
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
        LogoInfoDTO getLogoInfoById(@Param("id") Long id);

        /**
         * 新增
         * @param bean
         */
        Integer saveLogoInfo(LogoInfoDTO bean);

        /**
         * 根据ID修改
         * @param bean
         */
        Integer updateLogoInfoById(LogoInfoDTO bean);

        /**
         * 根据ID删除
         * @param id
         */
        Integer deleteLogoInfoById(@Param("id") Long id);

        /**
         * 根據商铺ID获取店招信息
         * @param shopsId
         * @return
         */
        LogoInfoDTO getLogoInfoByShopsIdAPI(Long shopsId);

}