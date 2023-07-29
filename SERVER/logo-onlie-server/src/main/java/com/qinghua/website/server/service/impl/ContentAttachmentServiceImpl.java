package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.ContentMapper;
import com.qinghua.website.server.domain.ContentAttachmentDTO;
import com.qinghua.website.server.domain.ContentDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.ContentAttachmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.qinghua.website.server.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.qinghua.website.server.dao.ContentAttachmentMapper;

import java.io.File;
import java.util.List;
import javax.annotation.Resource;

/**
 * 文章内容附件信息表 服务实现类
 * @since 2023-06-28
 */
@Service
public class ContentAttachmentServiceImpl implements ContentAttachmentService {

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private ContentAttachmentMapper contentAttachmentMapper;

    /**
     * 文章附件上传路径地址
     */
    @Value("${uploadPath.savePath}")
    private String savePath;

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

    /**
     * 根据附件名称删除附件信息
     * @param attachmentName
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteAttachmentByName(String attachmentName){
        //校验数据合法性
        ContentAttachmentDTO attachmentDTO = contentAttachmentMapper.getAttachmentByAttachmentName(attachmentName);
        if(null != attachmentDTO){
            //判断当前文章的审核状态，只有草稿和审核中的文章允许修改
            ContentDTO res = contentMapper.getContentById(attachmentDTO.getContentId());
            Preconditions.checkNotNull(res,"不存在文章ID为{}的信息",attachmentDTO.getId());
            if(null != res && (res.getStatus().equals("1") || res.getStatus().equals("0"))){
                //先执行服务器文件清除动作
                String filePath = savePath + "content/" + File.separator + attachmentDTO.getAttachmentPath() + "/" + attachmentName;
                FileUtils.deleteFile(filePath);
                //数据库信息删除
                contentAttachmentMapper.deleteAttachmentByName(attachmentName);
            }else{
                throw new BizException(SysConstant.ERROR_CONTENT_ALREADY_TAKE_EFFECT);
            }
        }else{
            throw new BizException(SysConstant.ERROR_FILE_NOT_EXIST);
        }
    }

}
