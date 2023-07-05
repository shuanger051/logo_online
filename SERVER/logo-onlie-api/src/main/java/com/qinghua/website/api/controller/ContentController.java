package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.ContentVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.ContentAttachmentDTO;
import com.qinghua.website.server.domain.ContentCheckDTO;
import com.qinghua.website.server.domain.ContentDTO;
import com.qinghua.website.server.domain.ContentExtDTO;
import com.qinghua.website.server.service.ContentAttachmentService;
import com.qinghua.website.server.service.ContentCheckService;
import com.qinghua.website.server.service.ContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentCheckService contentCheckService;

    @Autowired
    private ContentAttachmentService attachmentService;

    /**
     * 分页获取文章列表信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页获取文章列表信息")
    @RequestMapping(value = "/getContentListByPage",method = RequestMethod.GET)
    public ResponseResult<Object> getContentListByPage(@Validated @RequestBody ContentQueryIO bean){
        ContentDTO contentDTO = BeanToolsUtil.copyOrReturnNull(bean,ContentDTO.class);
        PageInfo<ContentDTO> pageList = contentService.getContentListByPage(contentDTO);
        List<ContentVO> contentVOList =  BeanToolsUtil.copyAsList(pageList.getList(), ContentVO.class);
        PageListVO<ContentVO> result = new PageListVO<>();
        result.setList(contentVOList);
        result.setTotal(pageList.getTotal());
        return ResponseResult.success(result);
    }

    /**
     * 根据文章ID查询文章信息
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据文章ID查询文章信息")
    @RequestMapping(value = "/getContentById",method = RequestMethod.GET)
    public ResponseResult<Object> getContentById(@RequestParam("id") Long id){
        ContentDTO contentDTO = contentService.getContentById(id);
        ContentVO contentVO = BeanToolsUtil.copyOrReturnNull(contentDTO,ContentVO.class);
        return ResponseResult.success(contentVO);
    }

    /**
     * 保存文章信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "保存文章信息")
    @RequestMapping(value = "/saveContent",method = RequestMethod.POST)
    public ResponseResult<Object> saveContent(@Validated @RequestBody ContentSaveIO bean){

        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setChannelId(bean.getChannelId());
        contentDTO.setIsRecommend(bean.getIsRecommend());

        ContentExtDTO contentExt = new ContentExtDTO();
        contentExt.setAuthor(bean.getContentExt().getAuthor());
        contentExt.setContent(bean.getContentExt().getContent());
        contentExt.setContentImg(bean.getContentExt().getContentImg());
        contentExt.setDescription(bean.getContentExt().getDescription());
        contentExt.setOrigin(bean.getContentExt().getOrigin());
        contentExt.setOriginUrl(bean.getContentExt().getOriginUrl());
        contentExt.setReleaseDate(bean.getContentExt().getReleaseDate());
        contentExt.setExternalLink(bean.getContentExt().getExternalLink());
        contentExt.setShortTitle(bean.getContentExt().getShortTitle());
        contentExt.setTitle(bean.getContentExt().getTitle());

        List<ContentAttachmentDTO> list = new ArrayList<>();
        if(null != bean.getContentAttachment() && bean.getContentAttachment().size() >0){
            for(int i=0;i<bean.getContentAttachment().size();i++){
                ContentAttachmentSaveIO saveIO = bean.getContentAttachment().get(i);
                ContentAttachmentDTO contentAttachment = new ContentAttachmentDTO();
                contentAttachment.setAttachmentPath(saveIO.getAttachmentPath());
                contentAttachment.setAttachmentName(saveIO.getAttachmentName());
                contentAttachment.setFilename(saveIO.getFilename());
                contentAttachment.setPriority(saveIO.getPriority());
                list.add(contentAttachment);
            }
        }

        contentService.saveContent(contentDTO,contentExt,list);
        return ResponseResult.success();
    }

    /**
     * 根据ID更新文章信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "根据ID更新文章信息")
    @RequestMapping(value = "/updateContentById",method = RequestMethod.POST)
    public ResponseResult<Object> updateContentById(@Validated @RequestBody ContentUpdateIO bean){

        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setId(bean.getId());
        contentDTO.setChannelId(bean.getChannelId());
        contentDTO.setIsRecommend(bean.getIsRecommend());

        ContentExtDTO contentExt = new ContentExtDTO();
        contentExt.setAuthor(bean.getContentExt().getAuthor());
        contentExt.setContent(bean.getContentExt().getContent());
        contentExt.setContentImg(bean.getContentExt().getContentImg());
        contentExt.setDescription(bean.getContentExt().getDescription());
        contentExt.setOrigin(bean.getContentExt().getOrigin());
        contentExt.setOriginUrl(bean.getContentExt().getOriginUrl());
        contentExt.setReleaseDate(bean.getContentExt().getReleaseDate());
        contentExt.setExternalLink(bean.getContentExt().getExternalLink());
        contentExt.setShortTitle(bean.getContentExt().getShortTitle());
        contentExt.setTitle(bean.getContentExt().getTitle());
        contentExt.setContentId(bean.getId());

        List<ContentAttachmentDTO> list = new ArrayList<>();
        if(null != bean.getContentAttachment() && bean.getContentAttachment().size() >0){
            for(int i=0;i<bean.getContentAttachment().size();i++){
                ContentAttachmentSaveIO saveIO = bean.getContentAttachment().get(i);
                ContentAttachmentDTO contentAttachment = new ContentAttachmentDTO();
                contentAttachment.setAttachmentPath(saveIO.getAttachmentPath());
                contentAttachment.setAttachmentName(saveIO.getAttachmentName());
                contentAttachment.setFilename(saveIO.getFilename());
                contentAttachment.setPriority(saveIO.getPriority());
                contentAttachment.setContentId(bean.getId());
                list.add(contentAttachment);
            }
        }

       contentService.updateContentById(contentDTO,contentExt,list);
        return ResponseResult.success();
    }

    /**
     * 根据ID删除文章信息
     * @param req
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "根据ID删除文章信息")
    @RequestMapping(value="/deleteContentById", method=RequestMethod.POST)
    public ResponseResult<Object> deleteContentById(@Valid @RequestBody IdIO req) {
        contentService.deleteContentById(req.getId());
        return ResponseResult.success();
    }

    /**
     * 根据附件名称删除附件信息
     * @param attachmentName
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "根据附件名称删除附件信息")
    @RequestMapping(value="/deleteAttachmentByName", method=RequestMethod.POST)
    public ResponseResult<Object> deleteAttachmentByName(@RequestParam("attachmentName")String attachmentName) {
        attachmentService.deleteAttachmentByName(attachmentName);
        return ResponseResult.success();
    }


    /**
     * 审核文章信息
     * @param checkUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "审核文章信息")
    @RequestMapping(value = "/auditContent", method = RequestMethod.POST)
    public ResponseResult<Object> auditContent(@Validated @RequestBody ContentCheckUpdateIO checkUpdateIO){
        ContentCheckDTO contentCheckDTO = BeanToolsUtil.copyOrReturnNull(checkUpdateIO,ContentCheckDTO.class);
        contentCheckService.updateContentCheckByContentId(contentCheckDTO);
        return ResponseResult.success();
    }

}
