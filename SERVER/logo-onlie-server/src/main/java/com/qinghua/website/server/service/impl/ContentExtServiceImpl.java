package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.domain.ContentExtDTO;
import com.qinghua.website.server.service.ContentExtService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.ContentExtMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 * 文章内容扩展表 服务实现类
 * @since 2023-06-28
 */
@Service
public class ContentExtServiceImpl implements ContentExtService {

                
        @Resource
        private ContentExtMapper contentExtMapper;

        @Override
        public List<ContentExtDTO> getContentExtList(ContentExtDTO bean) {
            return contentExtMapper.getContentExtList(bean);
        }

        @Override
        public PageInfo<ContentExtDTO> getContentExtListByPage(ContentExtDTO bean) {
            PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
            List<ContentExtDTO> contentExtList = contentExtMapper.getContentExtList(bean);
            return PageInfo.of(contentExtList);
        }

        @Override
        public ContentExtDTO getContentExtById(Long id) {
            Preconditions.checkNotNull(id, "参数:ID不能为空");
            return contentExtMapper.getContentExtById(id);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        @Override
        public Integer saveContentExt(ContentExtDTO bean) {
            return contentExtMapper.saveContentExt(bean);
        }

        @Override
        public Integer deleteContentExtById(Long id) {
            Preconditions.checkNotNull(id, "参数:ID不能为空");
            return contentExtMapper.deleteContentExtById(id);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        @Override
        public Integer updateContentExtById(ContentExtDTO bean) {
            Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
            return contentExtMapper.updateContentExtById(bean);
        }
}
