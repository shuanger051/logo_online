package com.qinghua.website.server.service;

import com.qinghua.website.server.domain.LogInfoDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 业务访问日志表（60天一清除） 服务类
 */
public interface LogInfoService {

        /**
        * 分页查询
        */
        PageInfo<LogInfoDTO> getLogInfoListByPage(LogInfoDTO bean);

        /**
         * 查询List集合
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
        LogInfoDTO getLogInfoById(Long id);

        /**
         * 新增
         * @param bean
         */
        Integer saveLogInfo(LogInfoDTO bean);

}
