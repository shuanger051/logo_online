package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.domain.ContentCheckDTO;
import com.qinghua.website.server.service.ContentCheckService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.ContentCheckMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 * 内容审核信息表 服务实现类
 * @since 2023-06-28
 */
@Service
public class ContentCheckServiceImpl implements ContentCheckService {

                
        @Resource
        private ContentCheckMapper contentCheckMapper;

        @Override
        public List<ContentCheckDTO> getContentCheckList(ContentCheckDTO bean) {
            return contentCheckMapper.getContentCheckList(bean);
        }

        @Override
        public PageInfo<ContentCheckDTO> getContentCheckListByPage(ContentCheckDTO bean) {
            PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
            List<ContentCheckDTO> contentCheckList = contentCheckMapper.getContentCheckList(bean);
            return PageInfo.of(contentCheckList);
        }

        @Override
        public ContentCheckDTO getContentCheckById(Long id) {
            Preconditions.checkNotNull(id, "参数:ID不能为空");
            return contentCheckMapper.getContentCheckById(id);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        @Override
        public Integer saveContentCheck(ContentCheckDTO bean) {
            return contentCheckMapper.saveContentCheck(bean);
        }

        @Override
        public Integer deleteContentCheckById(Long id) {
            Preconditions.checkNotNull(id, "参数:ID不能为空");
            return contentCheckMapper.deleteContentCheckById(id);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        @Override
        public Integer updateContentCheckById(ContentCheckDTO bean) {
            Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
            return contentCheckMapper.updateContentCheckById(bean);
        }
}
