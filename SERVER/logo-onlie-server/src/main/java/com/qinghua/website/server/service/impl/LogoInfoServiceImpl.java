package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.domain.LogoInfoDTO;
import com.qinghua.website.server.service.LogoInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.LogoInfoMapper;

import java.util.List;
import javax.annotation.Resource;

@Service
public class LogoInfoServiceImpl implements LogoInfoService {

                
    @Resource
    private LogoInfoMapper logoInfoMapper;

    @Override
    public List<LogoInfoDTO> getLogoInfoList(LogoInfoDTO bean) {
        return logoInfoMapper.getLogoInfoList(bean);
    }

    @Override
    public PageInfo<LogoInfoDTO> getLogoInfoListByPage(LogoInfoDTO bean) {
        PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
        List<LogoInfoDTO> logoInfoList = logoInfoMapper.getLogoInfoList(bean);
        return PageInfo.of(logoInfoList);
    }

    @Override
    public LogoInfoDTO getLogoInfoById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        return logoInfoMapper.getLogoInfoById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveLogoInfo(LogoInfoDTO bean) {
        logoInfoMapper.saveLogoInfo(bean);
    }

    @Override
    public void deleteLogoInfoById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        logoInfoMapper.deleteLogoInfoById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateLogoInfoById(LogoInfoDTO bean) {
        Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
        logoInfoMapper.updateLogoInfoById(bean);
    }
}
