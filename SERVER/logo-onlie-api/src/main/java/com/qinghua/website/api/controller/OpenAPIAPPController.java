package com.qinghua.website.api.controller;

import cn.hutool.core.lang.UUID;
import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.github.pagehelper.PageInfo;
import com.hazelcast.util.MD5Util;
import com.hazelcast.util.Preconditions;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.common.TokenTools;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.*;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.*;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.*;
import com.qinghua.website.server.utils.RSACryptoHelper;
import com.qinghua.website.server.utils.Sm4Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.misc.BASE64Decoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/app")
public class OpenAPIAPPController {

    @Value("${uploadPath.savePath}")
    private String savePath;   //图标物理存储路径

    @Value("${uploadPath.urlPath}")
    private String urlPath;   //图标映射路径

    @Autowired
    private CustomerInfoService customerInfoService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private ShopsInfoService shopsInfoService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private LogoInfoService logoInfoService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private SysDictItemService sysDictItemService;

    @Autowired
    private ContentAttachmentService attachmentService;

    /**
     * APP注册客户信息
     * @param customerSaveIO
     * @return
     */
    @LogAnnotation(logType = "register",logDesc = "APP注册客户信息")
    @RequestMapping("/registerCustomerAPI")
    public ResponseResult<Object> registerCustomerAPI(@Validated @RequestBody CustomerSaveIO customerSaveIO){
        CustomerInfoDTO save = BeanToolsUtil.copyOrReturnNull(customerSaveIO,CustomerInfoDTO.class);

        if(null != save && null != save.getPassword()){
            save.setPassword(MD5Util.toMD5String(save.getPassword()));
        }
        if(null != save && null != save.getMobile()){
            save.setMobile(Sm4Utils.encrypt(save.getMobile()));
        }
        if (null != save && null != save.getIdCard()){
            save.setIdCard(Sm4Utils.encrypt(save.getIdCard()));
        }

        customerInfoService.saveCustomerInfo(save);
        return ResponseResult.success();
    }

    /**
     * 用户登录APP获取TOKEN
     * @param loginIO
     * @param request
     * @return
     */
    @LogAnnotation(logType = "login",logDesc = "用户登录APP获取TOKEN")
    @RequestMapping(value = "/getTokenAPI",method = RequestMethod.POST)
    public ResponseResult<Object> getTokenAPI(@Validated  @RequestBody APPLoginIO loginIO, HttpServletRequest request){

        //先将前端传进来的RSA密码进行解密
        String password = RSACryptoHelper.decrypt(loginIO.getPassword());
        //将密码转为MD5
        String pwd = StringUtils.lowerCase(MD5Util.toMD5String(password));

        //先到数据库校验数据合法性，若不合法则不生成token，方便前端页面处理
        CustomerInfoDTO checkDTO = customerInfoService.checkCustomerByPWDAndAccount(loginIO.getCustomerName(),pwd);

        if(null != checkDTO){
            //根据账号密码生成TOKEN
            String token = TokenTools.genToken(loginIO.getCustomerName(),pwd);
            log.info("APP登录成功,返回token:{}",token);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("token",token);

            //添加用户信息返回
            CustomerInfoVO vo = BeanToolsUtil.copyOrReturnNull(checkDTO,CustomerInfoVO.class);
            if(null != vo && null != vo.getMobile()){
                vo.setMobile(Sm4Utils.decrypt(vo.getMobile()));
            }
            if(null != vo && null != vo.getIdCard()){
                vo.setIdCard(Sm4Utils.decrypt(vo.getIdCard()));
            }
            map.put("customerInfo",vo);

            return ResponseResult.success(map);
        }else{
            log.info("APP登录失败,返回null");
            return ResponseResult.error("账户密码错误!");
        }

    }

    /**
     * 根据ID查询客户信息
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询客户信息API")
    @RequestMapping("/queryCustomerByIdAPI")
    public ResponseResult<Object> queryCustomerByIdAPI(@RequestParam("id") Long id){
        CustomerInfoDTO customer = customerInfoService.getCustomerInfoById(id);
        CustomerInfoVO vo = BeanToolsUtil.copyOrReturnNull(customer, CustomerInfoVO.class);
        if(null != vo && null != vo.getMobile()){
            vo.setMobile(Sm4Utils.decrypt(vo.getMobile()));
        }
        if(null != vo && null != vo.getIdCard()){
            vo.setIdCard(Sm4Utils.decrypt(vo.getIdCard()));
        }
        return ResponseResult.success(vo);
    }

    /**
     * 修改客户信息
     */
    @LogAnnotation(logType = "update",logDesc = "APP客户信息修改")
    @RequestMapping("/updateCustomerAPI")
    public ResponseResult<Object> updateCustomerAPI(@Validated @RequestBody CustomerUpdateIO customerUpdateIO){
        CustomerInfoDTO customer = BeanToolsUtil.copyOrReturnNull(customerUpdateIO,CustomerInfoDTO.class);
        customerInfoService.updateCustomerInfoById(customer);
        return ResponseResult.success();
    }

    /**
     * 修改客户密码
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "APP客户密码修改")
    @RequestMapping("/updateCustomerPwdAPI")
    public ResponseResult<Object> updateCustomerPwdAPI(@Validated @RequestBody CustomerPwdUpdateIO customerPwdUpdateIO){
        customerInfoService.updateCustomerPwdByID(customerPwdUpdateIO.getId(),
                customerPwdUpdateIO.getOldPassword(),
                customerPwdUpdateIO.getNewPassword());
        return ResponseResult.success();
    }

    /**
     * 根据用户名称查询用户信息
     * @param customerName
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据用户名称查询用户信息")
    @RequestMapping("/getCustomerInfoByUserNameAPI")
    public ResponseResult<Object> getCustomerInfoByUserNameAPI(@RequestParam("customerName")String customerName){

        Preconditions.checkNotNull(customerName,"参数：customerName 不能为空");
        CustomerInfoDTO customer = customerInfoService.getCustomerByCustomerName(customerName);

        if(null != customer){

            CustomerInfoVO customerInfoVO = BeanToolsUtil.copyOrReturnNull(customer,CustomerInfoVO.class);

            //根据customer信息查询商户个人信息，通过身份证号关联
            MerchantInfoDTO merchant = merchantInfoService.getMerchantInfoByIdCard(customer.getIdCard());

            MerchantInfoVO merchantInfoVO = BeanToolsUtil.copyOrReturnNull(merchant,MerchantInfoVO.class);
            if(null != merchantInfoVO && null != merchantInfoVO.getPhone()){
                merchantInfoVO.setPhone(Sm4Utils.decrypt(merchantInfoVO.getPhone()));
            }
            if(null != merchantInfoVO && null != merchantInfoVO.getIdCard()){
                merchantInfoVO.setIdCard(Sm4Utils.decrypt(merchantInfoVO.getIdCard()));
            }

            //根据商户信息查询到店铺信息，通过所属关系关联
            List<ShopsInfoDTO> shopsList = shopsInfoService.getShopsInfoByMerchantId(merchant == null ? 0L :merchant.getId());
            List<ShopsInfoVO> shopsInfoVOList = BeanToolsUtil.copyList(shopsList,ShopsInfoVO.class);

            if(null != shopsInfoVOList && 0 != shopsInfoVOList.size()){
                shopsInfoVOList.forEach(item -> {
                    //国密数据解密
                    if(null != item && null != item.getHandledByPhone()){
                        item.setHandledByPhone(Sm4Utils.decrypt(item.getHandledByPhone()));
                    }
                    if(null != item && null != item.getHandledByIdCard()){
                        item.setHandledByIdCard(Sm4Utils.decrypt(item.getHandledByIdCard()));
                    }

                    //拼接映射URL
                    item.getList().forEach(dom ->{
                        if(null != dom){
                            String relativeFileName = dom.getAttachmentPath()  + "/" +  dom.getAttachmentName() ;
                            dom.setUrlPath(urlPath+"shops/" + relativeFileName);
                        }
                    });
                });
            }

            Map<String,Object> map = new HashMap<>();
            map.put("customerInfo",customerInfoVO);
            map.put("merchant",merchantInfoVO);
            map.put("shopsList",shopsInfoVOList);

            return ResponseResult.success(map);

        }else{
            throw new BizException("没有该客户信息数据", SysConstant.SYSTEM_ERROR_400.getCode());
        }
    }

    /**
     * 登记商户信息
     * @param merchantInfoSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "登记商户信息API")
    @RequestMapping("/saveMerchantAPI")
    public ResponseResult<Object> saveMerchantAPI(@Validated @RequestBody MerchantInfoSaveIO merchantInfoSaveIO){
        MerchantInfoDTO merchant = BeanToolsUtil.copyOrReturnNull(merchantInfoSaveIO,MerchantInfoDTO.class);
        if(null != merchant && null != merchant.getPhone()){
            merchant.setPhone(Sm4Utils.encrypt(merchant.getPhone()));
        }
        if(null != merchant && null != merchant.getIdCard()) {
            merchant.setIdCard(Sm4Utils.encrypt(merchant.getIdCard()));
        }
        merchantInfoService.saveMerchantInfo(merchant);
        return ResponseResult.success();
    }

    /**
     * 调整商户信息
     * @param merchantInfoUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "调整商户信息API")
    @RequestMapping("/updateMerchantAPI")
    public ResponseResult<Object> updateMerchantAPI(@Validated @RequestBody MerchantInfoUpdateIO merchantInfoUpdateIO){
        MerchantInfoDTO merchant = BeanToolsUtil.copyOrReturnNull(merchantInfoUpdateIO,MerchantInfoDTO.class);
        if(null != merchant && null != merchant.getPhone()){
            merchant.setPhone(Sm4Utils.encrypt(merchant.getPhone()));
        }
        if(null != merchant && null != merchant.getIdCard()) {
            merchant.setIdCard(Sm4Utils.encrypt(merchant.getIdCard()));
        }
        merchantInfoService.updateMerchantInfoById(merchant);
        return ResponseResult.success();
    }

    /**
     * APP 登记商铺信息
     * @param shopsInfoSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "APP 登记商铺信息API")
    @RequestMapping(value = "/saveShopsInfoAPI",method = RequestMethod.POST)
    public ResponseResult<Object> saveShopsInfoAPI(@Validated @RequestBody ShopsInfoAPISaveIO shopsInfoSaveIO){
        ShopsInfoDTO shopsInfoDTO =  BeanToolsUtil.copyOrReturnNull(shopsInfoSaveIO, ShopsInfoDTO.class);
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByPhone() != null){
            shopsInfoDTO.setHandledByPhone(Sm4Utils.encrypt(shopsInfoDTO.getHandledByPhone()));
        }
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByIdCard() != null) {
            shopsInfoDTO.setHandledByIdCard(Sm4Utils.encrypt(shopsInfoDTO.getHandledByIdCard()));
        }

        List<ShopsAttachmentDTO> list = BeanToolsUtil.copyList(shopsInfoSaveIO.getList(),ShopsAttachmentDTO.class);
        shopsInfoService.saveShopsInfo(shopsInfoDTO,list);
        return ResponseResult.success();
    }

    /**
     * APP修改商铺信息
     * @param shopsInfoUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "APP 修改商铺信息API")
    @RequestMapping(value = "/updateShopsInfoAPI",method = RequestMethod.POST)
    public ResponseResult<Object> saveShopsInfoAPI(@Validated @RequestBody ShopsInfoAPIUpdateIO shopsInfoUpdateIO){
        ShopsInfoDTO shopsInfoDTO =  BeanToolsUtil.copyOrReturnNull(shopsInfoUpdateIO, ShopsInfoDTO.class);
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByPhone() != null){
            shopsInfoDTO.setHandledByPhone(Sm4Utils.encrypt(shopsInfoDTO.getHandledByPhone()));
        }
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByIdCard() != null) {
            shopsInfoDTO.setHandledByIdCard(Sm4Utils.encrypt(shopsInfoDTO.getHandledByIdCard()));
        }

        ShopsInfoDTO res = shopsInfoService.getShopsInfoById(shopsInfoUpdateIO.getId());
        if(null != res){
            List<ShopsAttachmentDTO> list = BeanToolsUtil.copyList(shopsInfoUpdateIO.getList(),ShopsAttachmentDTO.class);
            shopsInfoService.updateShopsInfoById(shopsInfoDTO,list);
            return ResponseResult.success();
        }else{
            return ResponseResult.success("商铺信息不存在");
        }
    }

    /**
     * APP 修改店铺备案状态API
     * @param shopsInfoStatusIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "APP 修改店铺备案状态API")
    @RequestMapping(value = "/updateShopsFilingsStatusAPI",method = RequestMethod.POST)
    public ResponseResult<Object> updateShopsFilingsStatusAPI(@Validated @RequestBody ShopsInfoStatusIO shopsInfoStatusIO){
        ShopsInfoDTO bean = BeanToolsUtil.copyOrReturnNull(shopsInfoStatusIO,ShopsInfoDTO.class);
        shopsInfoService.updateShopsFilingsStatusAPI(bean);
        return ResponseResult.success();
    }

    /**
     * APP 根据商铺Id获取商铺详情信API
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 根据商铺Id获取商铺详情信API")
    @RequestMapping(value = "/getShopsInfoByIdAPI",method = RequestMethod.GET)
    public ResponseResult<Object> getShopsInfoByIdAPI(@RequestParam("shopsId")Long shopsId){
        Preconditions.checkNotNull(shopsId,"参数：shopsId,不能为空");
        ShopsInfoDTO res = shopsInfoService.getShopsInfoByIdAPI(shopsId);

        res.getList().forEach(item ->{
            String relativeFileName = item.getAttachmentPath()  + "/" +  item.getAttachmentName() ;
            item.setUrlPath(urlPath+"shops/" + relativeFileName);
        });

        ShopsInfoAPIVO vo = BeanToolsUtil.copyOrReturnNull(res,ShopsInfoAPIVO.class);
        if(null != vo && null != vo.getHandledByPhone()){
            vo.setHandledByPhone(Sm4Utils.decrypt(vo.getHandledByPhone()));
        }
        if(null != vo && null != vo.getHandledByIdCard()){
            vo.setHandledByIdCard(Sm4Utils.decrypt(vo.getHandledByIdCard()));
        }

        return ResponseResult.success(vo);
    }

    /**
     * 分页查询模板信息列表API
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询模板信息列表API")
    @RequestMapping("/queryTemplateListPageAPI")
    public ResponseResult<Object> queryTemplateListPageAPI(@Validated TemplateQueryAPIIO templateQueryIO){
        TemplateDTO template = BeanToolsUtil.copyOrReturnNull(templateQueryIO,TemplateDTO.class);
        PageInfo<TemplateDTO> resList = templateService.queryTemplateListPageAPI(template);
        List<TemplateVO> templateVOS = BeanToolsUtil.copyList(resList.getList(),TemplateVO.class);
        PageListVO<TemplateVO> result = new PageListVO<>();
        result.setList(templateVOS);
        result.setTotal(resList.getTotal());
        return ResponseResult.success(result);
    }

    /**
     * APP 根据ID查询模板详情API
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 根据ID查询模板详情API")
    @RequestMapping("/queryTemplateByIdAPI")
    public ResponseResult<Object> queryTemplateByIdAPI(@RequestParam("id") Long id){
        TemplateDTO template = templateService.getTemplateByIdAPI(id);
        TemplateVO templateVO = BeanToolsUtil.copyOrReturnNull(template,TemplateVO.class);
        return ResponseResult.success(templateVO);
    }

    /**
     * APP 随机查询简单模板数据API
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 随机查询简单模板数据API")
    @RequestMapping("/querySimpleTemplateByRandAPI")
    public ResponseResult<Object> querySimpleTemplateByRandAPI(@Validated TemplateQueryAPIIO templateQueryIO){
        TemplateDTO template = BeanToolsUtil.copyOrReturnNull(templateQueryIO,TemplateDTO.class);
        PageInfo<TemplateDTO> resList = templateService.querySimpleTemplateByRandAPI(template);
        List<TemplateVO> templateVOS = BeanToolsUtil.copyList(resList.getList(),TemplateVO.class);
        PageListVO<TemplateVO> result = new PageListVO<>();
        result.setList(templateVOS);
        result.setTotal(resList.getTotal());
        return ResponseResult.success(result);
    }

    /**
     * APP上传店招信息
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP上传店招信息API")
    @RequestMapping(value = "/saveLogoInfoAPI", method = RequestMethod.POST)
    public ResponseResult<Object> saveLogoInfoAPI(@RequestPart("file") MultipartFile multipartFile, Long shopsId, Long merchantId , HttpServletRequest request) {

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.split("\\.")[1];
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            boolean mkdirs = new File( savePath + "/logo/" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;

            String relativeFileName = frontPath + "/" + newFileName  ;
            String fullName = savePath + "/logo/" + relativeFileName;

            File file = new File(fullName);
            multipartFile.transferTo(file);

            if(null != shopsId && null != merchantId){
                LogoInfoDTO logoInfoDTO = new LogoInfoDTO();
                logoInfoDTO.setShopsId(shopsId);
                logoInfoDTO.setMerchantId(merchantId);
                logoInfoDTO.setLogoName(fileName);
                logoInfoDTO.setLogoFileName(newFileName);
                logoInfoDTO.setLogoFilePath(frontPath);

                //根据shopsId判断数据是新增还是更新
                LogoInfoDTO res = logoInfoService.getLogoInfoByShopsIdAPI(shopsId);
                if(null != res){
                    logoInfoDTO.setId(res.getId());
                    logoInfoService.updateLogoInfoById(logoInfoDTO);
                }else{
                    logoInfoService.saveLogoInfo(logoInfoDTO);
                }
            }

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(frontPath);
            fileVO.setAttachmentName(newFileName);
            fileVO.setUrlPath(urlPath+"logo/"+relativeFileName);
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            if(exception instanceof BizException){
                throw new BizException(exception.getMessage(),SysConstant.SYSTEM_ERROR_500.getCode());
            }else {
                throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
            }
        }
    }

    /**
     * APP 上传店招Base64文件信息API
     * @param base64
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 上传店招Base64文件信息API")
    @RequestMapping(value = "/saveLogoInfoBase64API", method = RequestMethod.POST)
    public ResponseResult<Object> saveLogoInfoBase64API(@RequestPart("base64") String base64, Long shopsId, Long merchantId , HttpServletRequest request) {

        Preconditions.checkNotNull(base64,"Base64String 不能为空");

        CommonsMultipartFile multipartFile = base64toMultipartFile(base64);

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.split("\\.")[1];
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            boolean mkdirs = new File( savePath + "/logo/" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;

            String relativeFileName = frontPath + "/" + newFileName  ;
            String fullName = savePath + "/logo/" + relativeFileName;

            File file = new File(fullName);
            multipartFile.transferTo(file);

            if(null != shopsId && null != merchantId){
                LogoInfoDTO logoInfoDTO = new LogoInfoDTO();
                logoInfoDTO.setShopsId(shopsId);
                logoInfoDTO.setMerchantId(merchantId);
                logoInfoDTO.setLogoName(fileName);
                logoInfoDTO.setLogoFileName(newFileName);
                logoInfoDTO.setLogoFilePath(frontPath);

                //根据shopsId判断数据是新增还是更新
                LogoInfoDTO res = logoInfoService.getLogoInfoByShopsIdAPI(shopsId);
                if(null != res){
                    logoInfoDTO.setId(res.getId());
                    logoInfoService.updateLogoInfoById(logoInfoDTO);
                }else{
                    logoInfoService.saveLogoInfo(logoInfoDTO);
                }
            }

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(frontPath);
            fileVO.setAttachmentName(newFileName);
            fileVO.setUrlPath(urlPath+"logo/"+relativeFileName);
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            if(exception instanceof BizException){
                throw new BizException(exception.getMessage(),SysConstant.SYSTEM_ERROR_500.getCode());
            }else {
                throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
            }
        }
    }

    /**
     * APP 分页查询查询店招图片列表API
     * @param logoQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 分页查询查询店招图片列表API")
    @RequestMapping(value = "/getLogoInfoListPageByNameAPI")
    public ResponseResult<Object> getLogoInfoListPageByNameAPI(@Validated LogoQueryIO logoQueryIO){
        LogoInfoDTO queryDTO = BeanToolsUtil.copyOrReturnNull(logoQueryIO,LogoInfoDTO.class);
        PageInfo<LogoInfoDTO> pageList =  logoInfoService.getLogoInfoListByPage(queryDTO);
        List<LogoInfoVO> logoInfoVOList =  BeanToolsUtil.copyAsList(pageList.getList(),LogoInfoVO.class);
        logoInfoVOList.forEach(item->item.setUrlPath(urlPath+"logo/"+item.getLogoFilePath()+"/"+item.getLogoFileName()));
        PageListVO<LogoInfoVO> resp = new PageListVO<>();
        resp.setList(logoInfoVOList);
        resp.setTotal(pageList.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * APP 根据商铺ID获取店招信息API
     * @param shopsId
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 根据商铺ID获取店招信息API")
    @RequestMapping(value = "/getLogoInfoByShopsIdAPI",method = RequestMethod.GET)
    public ResponseResult<Object> getLogoInfoByShopsIdAPI(@RequestParam("shopsId") Long shopsId){
        LogoInfoDTO res = logoInfoService.getLogoInfoByShopsIdAPI(shopsId);
        if(null != res){
            LogoInfoVO logoInfoVO = BeanToolsUtil.copyOrReturnNull(res,LogoInfoVO.class);
            String relativeFileName = logoInfoVO.getLogoFilePath() + "/" + logoInfoVO.getLogoFileName();
            logoInfoVO.setUrlPath(urlPath+"logo/"+relativeFileName);
            return ResponseResult.success(logoInfoVO);
        }else{
           return ResponseResult.success("未查询到数据");
        }
    }

    /**
     * 根据ID查询文章详情
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询文章详情")
    @RequestMapping("/getContentByIDAPI")
    public ResponseResult<Object> getContentByIDAPI(@RequestParam("id")Long id){
        ContentDTO res = contentService.getContentByIDAPI(id);
        ContentVO vo = BeanToolsUtil.copyOrReturnNull(res,ContentVO.class);
        return ResponseResult.success(vo);
    }

    /**
     * APP 分頁查询素材信息API
     * @param materialQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 分頁查询素材信息API")
    @RequestMapping("/getMaterialListByPageAPI")
    public ResponseResult<Object> getMaterialListByPageAPI(@Validated MaterialQueryIO materialQueryIO){
        MaterialDTO queryDTO = BeanToolsUtil.copyOrReturnNull(materialQueryIO,MaterialDTO.class);
        PageInfo<MaterialDTO> pageList =  materialService.getMaterialListByPage(queryDTO);
        List<MaterialVO> materialVOList =  BeanToolsUtil.copyAsList(pageList.getList(),MaterialVO.class);
        materialVOList.forEach(item->item.setUrlPath("/savePath/material/" + item.getFilePath() + "/" + item.getFileName()));
        PageListVO<MaterialVO> resp = new PageListVO<>();
        resp.setList(materialVOList);
        resp.setTotal(pageList.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * APP 上传商铺附件
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 上传商铺附件API")
    @RequestMapping(value = "/uploadShopsAttachmentAPI", method = RequestMethod.POST)
    public ResponseResult<Object> uploadShopsAttachmentAPI(@RequestPart("file")  MultipartFile multipartFile,Long shopsId,String attachmentType, HttpServletRequest request) {

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.split("\\.")[1];
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            boolean mkdirs = new File( savePath + "/shops/" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;

            String relativeFileName = frontPath + "/" + newFileName  ;
            String fullName = savePath + "/shops/" + relativeFileName;

            File file = new File(fullName);
            multipartFile.transferTo(file);

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
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            if(exception instanceof BizException){
                throw new BizException(exception.getMessage(),SysConstant.SYSTEM_ERROR_500.getCode());
            }else {
                throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
            }
        }
    }

    /**
     * APP 数据字典通过dict_key查询字典子项列表API(數據庫)
     * @param dictKey
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 数据字典通过dict_key查询字典子项列表API")
    @RequestMapping(value = "/getItemsByDictKeyInDB", method = RequestMethod.GET)
    public ResponseResult<Object> getItemsByDictKeyInDB(@RequestParam("dictKey") String dictKey) {
        List<SysDictItemDTO> sysDictItemList = sysDictItemService.getItemsByDictKeyInDB(dictKey);
        List<SysDictItemVO> sysDictItemVOList = BeanToolsUtil.copyAsList(sysDictItemList,SysDictItemVO.class);
        return ResponseResult.success(sysDictItemVOList);
    }

    /**
     * APP 下载文章附件API
     * @param attachmentName
     * @param request
     * @param response
     * @return
     */
    @LogAnnotation(logType = "download",logDesc = "APP 下载文章附件API")
    @RequestMapping(value = "/downloadContentAttachment", method = RequestMethod.GET)
    public void download(@RequestParam("attachmentName") String attachmentName, HttpServletRequest request, HttpServletResponse response) {

        Preconditions.checkNotNull(attachmentName,"参数：attachmentName 不能为空");

        try {
            ContentAttachmentDTO attachment = attachmentService.getAttachmentByAttachmentName(attachmentName);
            if(null != attachment && null != attachment.getAttachmentPath() && null != attachment.getAttachmentName()){
                String relativeFileName = attachment.getAttachmentPath() +"\\"+ attachment.getAttachmentName();

                String fullName = savePath + "content/" + relativeFileName;
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

    /**
     * APP 上传商铺Base64文件API
     * @param base64
     * @param shopsId
     * @param attachmentType
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 上传商铺Base64文件API")
    @RequestMapping(value = "/uploadContentAttachmentBase64", method = RequestMethod.POST)
    public ResponseResult<Object> uploadContentAttachmentBase64API(@RequestPart("base64")  String base64, Long shopsId, String attachmentType, HttpServletRequest request) {
        Preconditions.checkNotNull(base64,"Base64String 不能为空");

        CommonsMultipartFile multipartFile = base64toMultipartFile(base64);

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.split("\\.")[1];
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            boolean mkdirs = new File( savePath + "/shops/" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;

            String relativeFileName = frontPath + "/" + newFileName  ;
            String fullName = savePath + "/shops/" + relativeFileName;

            File file = new File(fullName);
            multipartFile.transferTo(file);

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
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            if(exception instanceof BizException){
                throw new BizException(exception.getMessage(),SysConstant.SYSTEM_ERROR_500.getCode());
            }else {
                throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
            }
        }

    }

    /**
     * Base64 转 MultipartFile
     * @param base64
     * @return
     * @throws IOException
     */
    public static CommonsMultipartFile base64toMultipartFile(String base64) {
        final File file = base64ToFile(base64);
        DiskFileItem item = (DiskFileItem) new DiskFileItemFactory().createItem(file.getName(), "image/jpeg", false, file.getName());
        InputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new BizException("Base64转MultipartFile失败",SysConstant.SYSTEM_ERROR_500.getCode());
        }
        OutputStream os = null;
        try {
            os = item.getOutputStream();
            if (input != null) {
                int ret = input.read();
                while (ret != -1) {
                    os.write(ret);
                    ret = input.read();
                }
                os.flush();
            }
        } catch (IOException e) {
            throw new BizException("Base64转MultipartFile失败",SysConstant.SYSTEM_ERROR_500.getCode());
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new BizException("Base64转MultipartFile失败",SysConstant.SYSTEM_ERROR_500.getCode());
                }
            }
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new BizException("Base64转MultipartFile失败",SysConstant.SYSTEM_ERROR_500.getCode());
                }
            }

        }
        return new CommonsMultipartFile(item);
    }

    /**
     * Base64 转 File
     * @param base64
     * @return
     */
    public static File base64ToFile(String base64) {

        if (base64 == null || "".equals(base64)) {
            throw new BizException("Base64参数不能为空",SysConstant.SYSTEM_ERROR_400.getCode());
        }

        String[] str = base64.split(",");
        //getRealImgeStr 方法 截取bese64字符串 逗号后半部分有用的字符串
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buff = new byte[0];
        try {
            buff = decoder.decodeBuffer(str[1]);
        } catch (IOException e) {
            throw new BizException("Base64转File失败",SysConstant.SYSTEM_ERROR_500.getCode());
        }
        File file = null;
        FileOutputStream fout = null;
        try {
            file = File.createTempFile("tmp", ".jpg");
            fout = new FileOutputStream(file);
            fout.write(buff);
        } catch (IOException e) {
            throw new BizException("Base64转File失败",SysConstant.SYSTEM_ERROR_500.getCode());
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    throw new BizException("Base64转File失败",SysConstant.SYSTEM_ERROR_500.getCode());
                }
            }
        }
        return file;
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
