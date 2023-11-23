package com.qinghua.website.api.controller;

import cn.hutool.core.lang.UUID;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.vo.FileVO;
import com.qinghua.website.api.utils.ImgUtils;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.*;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.ContentAttachmentService;
import com.qinghua.website.server.service.LogoInfoService;
import com.qinghua.website.server.service.MaterialService;
import com.qinghua.website.server.service.ShopsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 附件上传类
 */
@Slf4j
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Value("${uploadPath.savePath}")
    private String savePath;   //图标物理存储路径

    @Value("${uploadPath.urlPath}")
    private String urlPath;   //图标映射路径


    @Autowired
    private ContentAttachmentService attachmentService;

    @Autowired
    private ShopsInfoService shopsInfoService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private LogoInfoService logoInfoService;

    /**
     * 上传文章附件
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "上传文章附件")
    @RequestMapping(value = "/uploadContentAttachment", method = RequestMethod.POST)
    @RequiresPermissions("/attachment/uploadContentAttachment")
    public ResponseResult<Object> uploadContentAttachment(@RequestPart("file") MultipartFile multipartFile, Long contentId, HttpServletRequest request) {

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();

            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);

            if(null != fileType && !fileType.toLowerCase().equals("pdf") && !fileType.toLowerCase().equals("docx") && !fileType.toLowerCase().equals("txt")){
                throw new BizException("文章附件仅支持.PDF/.DOCX/.TXT这些格式文件",SysConstant.SYSTEM_ERROR_400.getCode());
            }

            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            boolean mkdirs = new File(savePath + "content/" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;

            String relativeFileName = frontPath + "/" + newFileName  ;
            String fullName = savePath + "content/" + relativeFileName;

            File file = new File(fullName);
            multipartFile.transferTo(file);

            if(null != contentId){
                ContentAttachmentDTO attachmentDTO = new ContentAttachmentDTO();
                attachmentDTO.setContentId(contentId);
                attachmentDTO.setContentId(1L);
                attachmentDTO.setFilename(fileName);
                attachmentDTO.setAttachmentName(newFileName);
                attachmentDTO.setAttachmentPath(frontPath);
                attachmentService.saveContentAttachment(attachmentDTO);
            }

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(frontPath);
            fileVO.setAttachmentName(newFileName);
            fileVO.setUrlPath(urlPath + "content/" + relativeFileName);
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            if (exception instanceof BizException){
                throw new BizException(exception.getMessage(),SysConstant.SYSTEM_ERROR_400.getCode());
            }else{
                throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
            }
        }
    }

    /**
     * 上传商铺附件
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "上传商铺附件")
    @RequestMapping(value = "/uploadShopsAttachment", method = RequestMethod.POST)
    @RequiresPermissions("/attachment/uploadShopsAttachment")
    public ResponseResult<Object> uploadShopsAttachment(@RequestPart("file")  MultipartFile multipartFile,Long shopsId,String attachmentType, HttpServletRequest request) {

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            boolean mkdirs = new File( savePath + "shops/" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;
            String newCompressFileName = fileId + "_COMPRESS." + fileType;

            String relativeFileName = frontPath + "/" + newFileName;
            String compressRelativeFileName = frontPath + "/" + newCompressFileName;
            String fullName = savePath + "shops/" + relativeFileName;
            String compressPath = savePath + "shops/" + frontPath;


            File file = new File(fullName);
            multipartFile.transferTo(file);

            //生成缩略图
            ImgUtils.localImageCompress(compressPath,fullName,newCompressFileName);

            if(null != shopsId){
                //更新数据库关系
                ShopsAttachmentDTO shopsAttachmentDTO = new ShopsAttachmentDTO();
                shopsAttachmentDTO.setShopsId(shopsId);
                shopsAttachmentDTO.setFileName(fileName);
                shopsAttachmentDTO.setAttachmentName(newFileName);
                shopsAttachmentDTO.setAttachmentPath(frontPath);
                shopsAttachmentDTO.setAttachmentType(attachmentType);

                List<ShopsAttachmentDTO> list = new ArrayList<>();
                list.add(shopsAttachmentDTO);
                shopsInfoService.saveShopsAttachments(list);
            }

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(frontPath);
            fileVO.setAttachmentName(newFileName);
            fileVO.setAttachmentType(attachmentType);
            fileVO.setUrlPath(urlPath+ "shops/" + relativeFileName);
            fileVO.setCompressUrlPath(urlPath+ "shops/" + compressRelativeFileName);
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
        }
    }

    /**
     * 上传素材附件
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "上传素材附件")
    @RequestMapping(value = "/uploadMaterialAttachment", method = RequestMethod.POST)
    @RequiresPermissions("/attachment/uploadMaterialAttachment")
    public ResponseResult<Object> uploadMaterialAttachment(@RequestPart("file")  MultipartFile multipartFile, HttpServletRequest request) {

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            boolean mkdirs = new File( savePath + "material/" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;

            String relativeFileName = frontPath + "/" + newFileName  ;
            String fullName = savePath + "material/" + relativeFileName;

            File file = new File(fullName);
            multipartFile.transferTo(file);

            //更新素材
            MaterialDTO materialDTO = new MaterialDTO();
            materialDTO.setName(fileName);
            materialDTO.setFileName(newFileName);
            materialDTO.setFilePath(frontPath);
            if(fileType.equals("JPEG") || fileType.equals("JPG") || fileType.equals("SVG") || fileType.equals("PNG")){
                materialDTO.setFileType("1");
            }else{
                materialDTO.setFileType("2");
            }

            materialService.saveMaterial(materialDTO);

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(frontPath);
            fileVO.setAttachmentName(newFileName);
            fileVO.setUrlPath(urlPath+ "material/" + relativeFileName);
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
        }
    }

    /**
     * 上传店招附件
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "上传店招附件")
    @RequestMapping(value = "/uploadLogoAttachment", method = RequestMethod.POST)
    @RequiresPermissions("/attachment/uploadLogoAttachment")
    public ResponseResult<Object> uploadLogoAttachment(@RequestPart("file")  MultipartFile multipartFile,Long shopsId,Long merchantId ,HttpServletRequest request) {

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            boolean mkdirs = new File( savePath + "logo/" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;
            String newCompressFileName = fileId + "_COMPRESS." + fileType;

            String relativeFileName = frontPath + "/" + newFileName  ;
            String compressRelativeFileName = frontPath + "/" + newCompressFileName;
            String fullName = savePath + "logo/" + relativeFileName;
            String compressPath = savePath + "logo/" + frontPath;

            File file = new File(fullName);
            multipartFile.transferTo(file);

            //生成缩略图
            ImgUtils.localImageCompress(compressPath,fullName,newCompressFileName);

            if(null != shopsId && null != merchantId){
                LogoInfoDTO logoInfoDTO = new LogoInfoDTO();
                logoInfoDTO.setShopsId(shopsId);
                logoInfoDTO.setMerchantId(merchantId);
                logoInfoDTO.setLogoName(fileName);
                logoInfoDTO.setLogoFileName(newFileName);
                logoInfoDTO.setLogoFilePath(frontPath);
                logoInfoService.saveLogoInfo(logoInfoDTO);
            }

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(frontPath);
            fileVO.setAttachmentName(newFileName);
            fileVO.setUrlPath(urlPath+"logo/"+relativeFileName);
            fileVO.setCompressUrlPath(urlPath+ "logo/" + compressRelativeFileName);
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
        }
    }

    /**
     * 校验文件是否合法
     * @param file
     * @return
     */
    public void checkFile(MultipartFile file){

        if (!file.isEmpty()) {
            //文件名称
            int begin = Objects.requireNonNull(file.getOriginalFilename()).indexOf(".");
            //文件名称长度
            int last = file.getOriginalFilename().length();
            if (file.getSize() > 5242880) {
                throw new IllegalArgumentException("上传文件不可超5M");
            }
        } else {
            throw new IllegalArgumentException("该文件无数据");
        }
    }

}
