package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.domain.ShopsInfoDTO;
import com.qinghua.website.server.service.ShopsInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.ShopsInfoMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 *  服务实现类
 * @since 2023-06-28
 */
@Service
public class ShopsInfoServiceImpl implements ShopsInfoService {

                
        @Resource
        private ShopsInfoMapper shopsInfoMapper;

        @Override
        public List<ShopsInfoDTO> getShopsInfoList(ShopsInfoDTO bean) {
            return shopsInfoMapper.getShopsInfoList(bean);
        }

        @Override
        public PageInfo<ShopsInfoDTO> getShopsInfoListByPage(ShopsInfoDTO bean) {
            PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
            List<ShopsInfoDTO> shopsInfoList = shopsInfoMapper.getShopsInfoList(bean);
            return PageInfo.of(shopsInfoList);
        }

        @Override
        public ShopsInfoDTO getShopsInfoById(Long id) {
            Preconditions.checkNotNull(id, "参数:ID不能为空");
            return shopsInfoMapper.getShopsInfoById(id);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        @Override
        public Integer saveShopsInfo(ShopsInfoDTO bean) {
            return shopsInfoMapper.saveShopsInfo(bean);
        }

        @Override
        public Integer deleteShopsInfoById(Long id) {
            Preconditions.checkNotNull(id, "参数:ID不能为空");
            return shopsInfoMapper.deleteShopsInfoById(id);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        @Override
        public Integer updateShopsInfoById(ShopsInfoDTO bean) {
            Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
            return shopsInfoMapper.updateShopsInfoById(bean);
        }
}
