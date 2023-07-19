package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.ContentAttachmentMapper;
import com.qinghua.website.server.dao.ContentCheckMapper;
import com.qinghua.website.server.dao.ContentExtMapper;
import com.qinghua.website.server.domain.ContentAttachmentDTO;
import com.qinghua.website.server.domain.ContentDTO;
import com.qinghua.website.server.domain.ContentExtDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.ContentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.qinghua.website.server.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.ContentMapper;

import java.io.File;
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

    @Resource
    private ContentCheckMapper checkMapper;

    /**
     * 文章附件上传路径地址
     */
    @Value("${upload.path.content}")
    private String contentPath;

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


    /**
     * APP文章详情查看
     * @param id
     * @return
     */
    @Override
    public ContentDTO getContentByIDAPI(Long id){
        ContentDTO res = contentMapper.getContentByIDAPI(id);
        if(null != res && null != res.getContentExt()){
            //更新浏览次数
            res.setViewsDay(res.getViewsDay()+1);
            contentMapper.updateContentById(res);
            return res;
        }else{
            throw new BizException("没有符合条件的已发布文章或文章未到预定发布时间",SysConstant.SYSTEM_ERROR_400.getCode());
        }
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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteContentById(Long id) {
        Preconditions.checkNotNull(id, "参数:ID不能为空");
        //删除文章信息
        contentMapper.deleteContentById(id);
        //关联删除附件信息
        //移除服务器上传的附件
        List<ContentAttachmentDTO> attachmentList = attachmentMapper.getAttachmentListByContentId(id);
        if(null != attachmentList && attachmentList.size() > 0){
            for(int i=0;i<attachmentList.size();i++){
                ContentAttachmentDTO attachment = attachmentList.get(i);
                String filePath = contentPath + File.separator + attachment.getAttachmentPath() + "\\" + attachment.getAttachmentName();
                FileUtils.deleteFile(filePath);
            }
        }
        attachmentMapper.deleteContentAttachmentByContentId(id);

        //关联删除审核信息
        checkMapper.deleteContentCheckByContentId(id);
        //关联删除扩展信息
        contentExtMapper.deleteContentExtByContentId(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateContentById(ContentDTO bean, ContentExtDTO contentExt, List<ContentAttachmentDTO> list) {

        //判断当前文章的审核状态，只有草稿和审核中的文章允许修改
        ContentDTO res = contentMapper.getContentById(bean.getId());
        Preconditions.checkNotNull(res,"不存在文章ID为{}的信息",bean.getId());
        if(null != res && (res.getStatus().equals("1") || res.getStatus().equals("0"))){
            contentMapper.updateContentById(bean);
            contentExtMapper.updateContentExtByContentId(contentExt);
            if(null != list && list.size() > 0){
                //先清空原来的附件信息
                attachmentMapper.deleteContentAttachmentByContentId(bean.getId());
                //批量新增
                attachmentMapper.saveContentAttachmentList(list);
            }
        }else{
            throw new BizException(SysConstant.ERROR_CONTENT_ALREADY_TAKE_EFFECT);
        }
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
