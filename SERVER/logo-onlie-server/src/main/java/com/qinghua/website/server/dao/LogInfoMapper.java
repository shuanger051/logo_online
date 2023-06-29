package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.LogInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 业务访问日志表（60天一清除） Mapper 接口
 */
@Mapper
public interface LogInfoMapper {

        /**
         * 查询列表集合
         * @param bean
         * @return
         */
        List<LogInfoDTO> getLogInfoList(LogInfoDTO bean);

        /**
         * 根据ID查询
         *
         * @param id
         * @return
         */
        LogInfoDTO getLogInfoById(@Param("id") Long id);

        /**
         * 新增
         * @param bean
         */
        Integer saveLogInfo(LogInfoDTO bean);

}