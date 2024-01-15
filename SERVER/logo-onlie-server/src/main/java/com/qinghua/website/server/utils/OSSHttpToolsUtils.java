package com.qinghua.website.server.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class OSSHttpToolsUtils {

    public static final String OSS_ENDPOINT = "https://oss-cn-hangzhou.aliyuncs.com";
    public static final String BUCKET_NAME = "img-save-dir";
    public static final String OSS_ACCESS_KEY_ID = "LTAI5tQ2nydShTmWfGqmP384";
    public static final String OSS_ACCESS_KEY_SECRET = "h6qPG28Jy05cAqGKGUiUUAB0xQp9kI";

    public static void main(String[] args) throws Exception {

        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        String objectName = "upload/material/2023/07/13/214312341.JPEG";
        String filePath = "F:/upload/material/2023/07/13/test.JPEG";
        //uploadFile(ossClient,objectName,"1234567hello");
        uploadFile(objectName,new File(filePath));
        getUrl(objectName);
    }

    /**
     * 创建bucket目录
     * @param bucketName
     * @return
     */
    public static void createBucket(String bucketName){
        OSS ossClient = initClient();
        try {
            ossClient.createBucket(bucketName);
        } catch (Exception oe) {
            log.error(oe.getMessage());
            throw new BizException("创建OSS Bucket时出现异常!", SysConstant.SYSTEM_ERROR_500.getCode());
        }finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 上传txt
     * @param objectName
     * @param content
     * @return
     */
    public static void uploadTxt(String objectName,String content){
        OSS ossClient = initClient();
        try {
            ossClient.putObject(BUCKET_NAME, objectName, new ByteArrayInputStream(content.getBytes()));
        } catch (Exception oe) {
            log.error(oe.getMessage());
            throw new BizException("OSS 文件上传时出现异常!", SysConstant.SYSTEM_ERROR_500.getCode());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 上传文件
     * @param objectName
     * @param file
     * @return
     */
    public static void uploadFile(String objectName,File file){
        OSS ossClient = initClient();
        try {
            ossClient.putObject(BUCKET_NAME, objectName, file);
        } catch (Exception oe) {
            log.error(oe.getMessage());
            throw new BizException("OSS 文件上传时出现异常!", SysConstant.SYSTEM_ERROR_500.getCode());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 获得url链接
     *
     * @param objectName
     * @return
     */
    public static String getUrl(String objectName) {
        // 设置URL过期时间为10年
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        OSS ossClient = initClient();
        URL url = ossClient.generatePresignedUrl(BUCKET_NAME,objectName , expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 初始化OSS client
     * @return
     */
    public static OSS initClient(){
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(OSSHttpToolsUtils.OSS_ENDPOINT, OSS_ACCESS_KEY_ID,OSS_ACCESS_KEY_SECRET);
        }catch (Exception e){
            log.error("无法从环境变量里获取OSS配置参数!"+e.getMessage());
            throw new BizException("初始化OSS客户端失败!",SysConstant.SYSTEM_ERROR_500.getCode());
        }
        return ossClient;
    }

    /**
     *
     * 上传图片
     * @param multipartFile
     * @return
     */
    public static String uploadImg2Oss(String fileDir,String fileName,MultipartFile multipartFile) {

        String fileUrl = "";
        try {
            InputStream inputStream = multipartFile.getInputStream();
            uploadFile2OSS(inputStream, fileDir ,fileName);
            fileUrl = getUrl(fileDir+"/"+fileName);
        } catch (Exception e) {
            log.error("OSS上传文件失败!");
            throw new BizException("OSS上传文件失败!",SysConstant.SYSTEM_ERROR_500.getCode());
        }
        return fileUrl;
    }

    /**
     * 校验文件是否合法
     * @param file
     * @return
     */
    public static void checkFile(MultipartFile file){

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

    /**
     * 上传图片获取fileUrl
     * @param instream
     * @param fileName
     * @return
     */
    private static String uploadFile2OSS(InputStream instream, String filedir,String fileName) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件

            OSS ossClient = initClient();
            PutObjectResult putResult = ossClient.putObject(BUCKET_NAME, filedir + "/" + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (Exception e) {
                log.error("OSS上传文件失败!");
                throw new BizException("OSS上传文件失败!",SysConstant.SYSTEM_ERROR_500.getCode());
            }
        }
        return ret;
    }

    /**
     * 获取文件类型
     * @param FilenameExtension
     * @return
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 删除OSS文件信息
     * @param objectName
     */
    public static void delOSSFile(String objectName){
        OSS ossClient = initClient();
        try {
            ossClient.deleteObject(BUCKET_NAME, objectName);
        } catch (Exception ce) {
            log.error("删除OSS文件失败!",ce.getMessage());
            throw new BizException("删除OSS文件失败!",SysConstant.SYSTEM_ERROR_500.getCode());
        } finally {
            ossClient.shutdown();
        }
    }

}
