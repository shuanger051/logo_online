package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.ShopsInfoMapper;
import com.qinghua.website.server.domain.MerchantInfoDTO;
import com.qinghua.website.server.domain.ShopsInfoDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.MerchantInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.MerchantInfoMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 *  服务实现类
 * @since 2023-06-28
 */
@Service
public class MerchantInfoServiceImpl implements MerchantInfoService {

                
    @Resource
    private MerchantInfoMapper merchantInfoMapper;

    @Resource
    private ShopsInfoMapper shopsInfoMapper;

    @Override
    public List<MerchantInfoDTO> getMerchantInfoList(MerchantInfoDTO bean) {
        return merchantInfoMapper.getMerchantInfoList(bean);
    }

    @Override
    public PageInfo<MerchantInfoDTO> getMerchantInfoListByPage(MerchantInfoDTO bean) {
        PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
        List<MerchantInfoDTO> merchantInfoList = merchantInfoMapper.getMerchantInfoList(bean);
        return PageInfo.of(merchantInfoList);
    }

    @Override
    public MerchantInfoDTO getMerchantInfoById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        return merchantInfoMapper.getMerchantInfoById(id);
    }

    @Override
    public void saveMerchantInfo(MerchantInfoDTO bean) {
        //根据身份证号校验是否重复
        MerchantInfoDTO checkDTO = merchantInfoMapper.getMerchantInfoByIdCard(bean.getIdCard());
        if(null != checkDTO){
            throw new BizException(SysConstant.ERROR_MERCHANT_IS_EXISTS);
        }
        merchantInfoMapper.saveMerchantInfo(bean);
    }

    @Override
    public void deleteMerchantInfoById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        //判断当前商户下是否有商铺信息
        List<ShopsInfoDTO> checkList = shopsInfoMapper.getShopsInfoByMerchantId(id);
        if(null != checkList && checkList.size() > 0){
            throw new BizException(SysConstant.ERROR_HAVE_SHOPSINFO);
        }
        merchantInfoMapper.deleteMerchantInfoById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMerchantInfoById(MerchantInfoDTO bean) {
        Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
        merchantInfoMapper.updateMerchantInfoById(bean);
    }

    /**
     * 根据身份证号查询商户信息
     * @param idCard
     * @return
     */
    @Override
    public MerchantInfoDTO getMerchantInfoByIdCard(String idCard){
        return merchantInfoMapper.getMerchantInfoByIdCard(idCard);
    }
}
