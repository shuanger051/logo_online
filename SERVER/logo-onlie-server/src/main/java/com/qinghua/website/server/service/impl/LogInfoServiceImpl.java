package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.domain.LogInfoDTO;
import com.qinghua.website.server.service.LogInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.LogInfoMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 * 业务访问日志表（60天一清除） 服务实现类
 */
@Service
public class LogInfoServiceImpl implements LogInfoService {

                
        @Resource
        private LogInfoMapper logInfoMapper;

        @Override
        public List<LogInfoDTO> getLogInfoList(LogInfoDTO bean) {
            return logInfoMapper.getLogInfoList(bean);
        }

        @Override
        public PageInfo<LogInfoDTO> getLogInfoListByPage(LogInfoDTO bean) {
            PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
            List<LogInfoDTO> logInfoList = logInfoMapper.getLogInfoList(bean);
            return PageInfo.of(logInfoList);
        }

        @Override
        public LogInfoDTO getLogInfoById(Long id) {
            Preconditions.checkNotNull(id, "参数:ID不能为空");
            return logInfoMapper.getLogInfoById(id);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        @Override
        public Integer saveLogInfo(LogInfoDTO bean) {
            return logInfoMapper.saveLogInfo(bean);
        }

}
