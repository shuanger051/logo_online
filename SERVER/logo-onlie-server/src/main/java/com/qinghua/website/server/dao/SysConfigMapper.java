package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.SysConfigDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置表 Mapper 接口
 */
@Mapper
public interface SysConfigMapper {

        /**
         * 根据ID查询
         *
         * @param key
         * @return
         */
        SysConfigDTO getSysConfigByKey(@Param("configKey")String key);

}