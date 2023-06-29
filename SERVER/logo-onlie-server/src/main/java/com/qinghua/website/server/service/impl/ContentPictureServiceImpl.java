package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.domain.ContentPictureDTO;
import com.qinghua.website.server.service.ContentPictureService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.ContentPictureMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 * 文章内容图片表 服务实现类
 * @since 2023-06-28
 */
@Service
public class ContentPictureServiceImpl implements ContentPictureService {

                
        @Resource
        private ContentPictureMapper contentPictureMapper;

        @Override
        public List<ContentPictureDTO> getContentPictureList(ContentPictureDTO bean) {
            return contentPictureMapper.getContentPictureList(bean);
        }

        @Override
        public PageInfo<ContentPictureDTO> getContentPictureListByPage(ContentPictureDTO bean) {
            PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
            List<ContentPictureDTO> contentPictureList = contentPictureMapper.getContentPictureList(bean);
            return PageInfo.of(contentPictureList);
        }

        @Override
        public ContentPictureDTO getContentPictureById(Long id) {
            Preconditions.checkNotNull(id, "参数:ID不能为空");
            return contentPictureMapper.getContentPictureById(id);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        @Override
        public Integer saveContentPicture(ContentPictureDTO bean) {
            return contentPictureMapper.saveContentPicture(bean);
        }

        @Override
        public Integer deleteContentPictureById(Long id) {
            Preconditions.checkNotNull(id, "参数:ID不能为空");
            return contentPictureMapper.deleteContentPictureById(id);
        }

        @Transactional(propagation = Propagation.REQUIRED)
        @Override
        public Integer updateContentPictureById(ContentPictureDTO bean) {
            Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
            return contentPictureMapper.updateContentPictureById(bean);
        }
}
