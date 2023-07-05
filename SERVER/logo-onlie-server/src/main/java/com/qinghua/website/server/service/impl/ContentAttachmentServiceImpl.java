package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.domain.ContentAttachmentDTO;
import com.qinghua.website.server.service.ContentAttachmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.ContentAttachmentMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 * 文章内容附件信息表 服务实现类
 * @since 2023-06-28
 */
@Service
public class ContentAttachmentServiceImpl implements ContentAttachmentService {

                
    @Resource
    private ContentAttachmentMapper contentAttachmentMapper;

    @Override
    public List<ContentAttachmentDTO> getContentAttachmentList(ContentAttachmentDTO bean) {
        return contentAttachmentMapper.getContentAttachmentList(bean);
    }

    /**
     * 根据
     * @param attachmentName
     * @return
     */
    public ContentAttachmentDTO getAttachmentByAttachmentName(String attachmentName){
        return contentAttachmentMapper.getAttachmentByAttachmentName(attachmentName);
    }


    @Override
    public ContentAttachmentDTO getContentAttachmentById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        return contentAttachmentMapper.getContentAttachmentById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer saveContentAttachment(ContentAttachmentDTO bean) {
        return contentAttachmentMapper.saveContentAttachment(bean);
    }

    @Override
    public Integer deleteContentAttachmentById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        return contentAttachmentMapper.deleteContentAttachmentById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer updateContentAttachmentById(ContentAttachmentDTO bean) {
        Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
        return contentAttachmentMapper.updateContentAttachmentById(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDownloadTimes(Long id){
        contentAttachmentMapper.updateDownloadTimes(id);
    }

}
