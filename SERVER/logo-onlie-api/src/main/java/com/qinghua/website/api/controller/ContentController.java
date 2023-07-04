package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.config.Constants;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.ContentVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.upload.FileRepository;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.api.utils.PdfBoxUtils;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.ContentCheckDTO;
import com.qinghua.website.server.domain.ContentDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.ContentCheckService;
import com.qinghua.website.server.service.ContentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentCheckService contentCheckService;

    /**
     * 文件存储路径
     */
    private String basePath;

    /**
     * 文件访问路径
     */
    private String viewPath;

    @Autowired
    private FileRepository fileRepository;

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
    public ResponseResult<Object> saveContent(@Validated ContentSaveIO bean){
        ContentDTO contentDTO = BeanToolsUtil.copyOrReturnNull(bean,ContentDTO.class);
        contentService.saveContent(contentDTO);
        return ResponseResult.success();
    }

    /**
     * 上传文章附件
     * @param file
     * @param transfer
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "上传文章附件")
    @RequestMapping(value = {"/uploadAttachment"},method = RequestMethod.POST)
    public ResponseResult<Object> uploadAttachment(@RequestParam(name = "attachments") MultipartFile[] file, boolean transfer){
        List<Map<String,Object>> result = new ArrayList<>();
        if(null != file && file.length > 0){
            Arrays.stream(file).forEach(f ->{
                try {
                    Map<String,Object> map = new HashMap<>();
                    String fileUrl = fileRepository.storeByExt(basePath + Constants.CONTENT_RESOURCES_PATH, FilenameUtils.getExtension(f.getOriginalFilename()).toLowerCase(),f);
                    File newFile = new File(fileUrl);
                    //根据是否需要将PDF转化为图片参数进行操作,把pdf转换成图片
                    if (transfer && FilenameUtils.isExtension(f.getOriginalFilename(),"pdf")){
                        List<String> urls = PdfBoxUtils.transfer2Img(newFile, FilenameUtils.getFullPath(newFile.getAbsolutePath()),FilenameUtils.getBaseName(newFile.getAbsolutePath()), PdfBoxUtils.IMG_TYPE_JPG,1500);
                        map.put("urls",urls.stream().map(url-> viewPath +  url.replace(basePath,"")).collect(Collectors.toList()));
                    }
                    map.put("attachmentPath",fileUrl.replace(basePath,""));
                    map.put("attachmentName",f.getOriginalFilename());
                    result.add(map);
                } catch (IOException e) {
                    throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
                }
            });
        }
        return ResponseResult.success(result);
    }

    /**
     * 根据ID更新文章信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "根据ID更新文章信息")
    @RequestMapping(value = "/updateContentById",method = RequestMethod.PUT)
    public ResponseResult<Object> updateContentById(@Validated @RequestBody ContentUpdateIO bean){
       ContentDTO contentDTO = BeanToolsUtil.copyOrReturnNull(bean,ContentDTO.class);
       contentService.updateContentById(contentDTO);
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
     * 审核文章信息
     * @param checkUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "审核文章信息")
    @RequestMapping(value = "/auditContent", method = RequestMethod.PUT)
    public ResponseResult<Object> auditContent(@Valid ContentCheckUpdateIO checkUpdateIO){
        ContentCheckDTO contentCheckDTO = BeanToolsUtil.copyOrReturnNull(checkUpdateIO,ContentCheckDTO.class);
        contentCheckService.updateContentCheckById(contentCheckDTO);
        return ResponseResult.success();
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

}
