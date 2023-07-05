package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.dao.ContentAttachmentMapper;
import com.qinghua.website.server.dao.ContentExtMapper;
import com.qinghua.website.server.domain.ContentAttachmentDTO;
import com.qinghua.website.server.domain.ContentDTO;
import com.qinghua.website.server.domain.ContentExtDTO;
import com.qinghua.website.server.service.ContentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.ContentMapper;

import java.util.List;
import javax.annotation.Resource;

/**
 * 文章信息表 服务实现类
 * @since 2023-06-28
 */
@Slf4j
@Service
public class ContentServiceImpl implements ContentService {

                
    @Resource
    private ContentMapper contentMapper;

    @Resource
    private ContentExtMapper contentExtMapper;

    @Resource
    private ContentAttachmentMapper attachmentMapper;

    @Override
    public List<ContentDTO> getContentList(ContentDTO bean) {
        return contentMapper.getContentList(bean);
    }

    @Override
    public PageInfo<ContentDTO> getContentListByPage(ContentDTO bean) {
        PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
        List<ContentDTO> contentList = contentMapper.getContentList(bean);
        return PageInfo.of(contentList);
    }

    @Override
    public ContentDTO getContentById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        return contentMapper.getContentById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveContent(ContentDTO bean, ContentExtDTO contentExt, List<ContentAttachmentDTO> list) {

        contentMapper.saveContent(bean);
        Long contentId = bean.getId();
        if(null != contentId){
            contentExt.setContentId(contentId);
            contentExtMapper.saveContentExt(contentExt);
            if(null != list){
                for(int i=0;i<list.size();i++){
                    ContentAttachmentDTO attachmentDTO = list.get(i);
                    attachmentDTO.setContentId(contentId);
                    attachmentMapper.saveContentAttachment(attachmentDTO);
                }
            }
        }
    }

    @Override
    public Long deleteContentById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        return contentMapper.deleteContentById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Long updateContentById(ContentDTO bean) {
        Preconditions.checkNotNull(bean.getId(), "参数:ID不能为空");
        return contentMapper.updateContentById(bean);
    }

    /**
     * 根据ChannelID查询文章结合
     * @param bean
     * @return
     */
    @Override
    public PageInfo<ContentDTO> getContentByChannelId(ContentDTO bean){
        PageHelper.startPage(bean.getPageNum(), bean.getPageSize());
        List<ContentDTO> contentList = contentMapper.getContentByChannelId(bean);
        return PageInfo.of(contentList);
    }

}
