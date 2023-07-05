package com.qinghua.website.server.service.impl;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.ContentMapper;
import com.qinghua.website.server.domain.ContentCheckDTO;
import com.qinghua.website.server.domain.ContentDTO;
import com.qinghua.website.server.exception.BizException;
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

    @Resource
    private ContentMapper contentMapper;

    @Override
    public List<ContentCheckDTO> getContentCheckList(ContentCheckDTO bean) {
        return contentCheckMapper.getContentCheckList(bean);
    }

    @Override
    public ContentCheckDTO getContentCheckByContentId(Long contentId) {
        Preconditions.checkNotNull(contentId, "参数:ContentID不能为空");
        return contentCheckMapper.getContentCheckByContentId(contentId);
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

    /**
     * 根据contentId更新审核信息
     * @param checkDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateContentCheckByContentId(ContentCheckDTO checkDTO){
        Preconditions.checkNotNull(checkDTO.getContentId(), "参数:ContentId不能为空");
        //校验数据合法性
        ContentCheckDTO res = contentCheckMapper.getContentCheckByContentId(checkDTO.getContentId());
        if(null != res){
            contentCheckMapper.updateContentCheckByContentId(checkDTO);
        }else {
            contentCheckMapper.saveContentCheck(checkDTO);
        }

        //根据审核结果更新文章状态, 1-退回 则文章状态更改为审核中【1】，0-不退回 则文章状态更改为审核通过【2】
        ContentDTO content = new ContentDTO();
        content.setId(checkDTO.getContentId());
        if(null != checkDTO.getIsRejected() && "1".equals(checkDTO.getIsRejected())){
            content.setStatus("1");
        }else{
            content.setStatus("2");
        }
        contentMapper.updateContentById(content);
    }
}
