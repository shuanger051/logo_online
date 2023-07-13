package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.ShopsInfoMapper;
import com.qinghua.website.server.domain.LogoInfoDTO;
import com.qinghua.website.server.domain.ShopsInfoDTO;
import com.qinghua.website.server.exception.BizException;
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

    @Resource
    private ShopsInfoMapper shopsInfoMapper;

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
        //校验shopsId 与 merchantID 是否对应。
        ShopsInfoDTO shops = shopsInfoMapper.getShopsInfoById(bean.getShopsId());
        if(null != shops && shops.getMerchantId().equals(bean.getMerchantId())){
            logoInfoMapper.saveLogoInfo(bean);
        }else{
            throw new BizException(SysConstant.ERROR_SHOPS_MERCHANT_IS_WRONG);
        }
    }

    @Override
    public void deleteLogoInfoById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        logoInfoMapper.deleteLogoInfoById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateLogoInfoById(LogoInfoDTO bean) {
        //校验shopsId 与 merchantID 是否对应。
        ShopsInfoDTO shops = shopsInfoMapper.getShopsInfoById(bean.getShopsId());
        if(null != shops && shops.getMerchantId().equals(bean.getMerchantId())){
            logoInfoMapper.updateLogoInfoById(bean);
        }else{
            throw new BizException(SysConstant.ERROR_SHOPS_MERCHANT_IS_WRONG);
        }
    }
}
