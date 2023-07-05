package com.qinghua.website.api.controller;

import cn.hutool.core.lang.UUID;
import com.google.common.base.Preconditions;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.vo.FileVO;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.ContentAttachmentDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.ContentAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 附件上传类
 */
@Slf4j
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    /**
     * 文章附件上传路径地址
     */
    @Value("${upload.path.content}")
    private String contentPath;

    /**
     * 商户附件上传路径
     */
    @Value("${upload.path.merchant}")
    private String merchantPath;


    @Autowired
    private ContentAttachmentService attachmentService;

    /**
     * 上传文章附件
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "上传文章附件")
    @RequestMapping(value = "/uploadContentAttachment", method = RequestMethod.POST)
    public ResponseResult<Object> uploadContentAttachment(@RequestPart("file") MultipartFile multipartFile, HttpServletRequest request) {
        try {
            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.split("\\.")[1];
            String frontPath = new SimpleDateFormat("yyyy\\MM\\dd").format(new Date());
            boolean mkdirs = new File(contentPath + "\\" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;

            String relativeFileName = frontPath + "\\" + newFileName  ;
            String fullName = contentPath + "\\" + relativeFileName;

            File file = new File(fullName);
            multipartFile.transferTo(file);

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(frontPath);
            fileVO.setAttachmentName(newFileName);
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
        }
    }

    /**
     * 下载文章附件
     * @param attachmentName
     * @param request
     * @param response
     * @return
     */
    @LogAnnotation(logType = "download",logDesc = "下载文章附件")
    @RequestMapping(value = "/downloadContentAttachment", method = RequestMethod.GET)
    public void download(@RequestParam("attachmentName") String attachmentName, HttpServletRequest request, HttpServletResponse response) {

        Preconditions.checkNotNull(attachmentName,"参数：attachmentName 不能为空");

        try {
            ContentAttachmentDTO attachment = attachmentService.getAttachmentByAttachmentName(attachmentName);
            if(null != attachment && null != attachment.getAttachmentPath() && null != attachment.getAttachmentName()){
                String relativeFileName = attachment.getAttachmentPath() +"\\"+ attachment.getAttachmentName();

                String fullName = contentPath + File.separator + relativeFileName;
                File file = new File(fullName);
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                response.reset();
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "utf-8"));
                response.setHeader("Content-Length", String.valueOf(file.length()));
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
                byte[] bytes = new byte[1024];
                int len;
                while ((len = bufferedInputStream.read(bytes)) > 0) {
                    bufferedOutputStream.write(bytes, 0, len);
                }
                bufferedInputStream.close();
                bufferedOutputStream.flush();

                //更新下载次数
                attachmentService.updateDownloadTimes(attachment.getId());
            }else{
                throw new BizException(SysConstant.ERROR_GET_ATTACHMENT_INFO_FAIL);
            }
        } catch (Exception exception) {
            throw new BizException(SysConstant.ERROR_DOWNLOAD_FAIL);
        }

    }

}
