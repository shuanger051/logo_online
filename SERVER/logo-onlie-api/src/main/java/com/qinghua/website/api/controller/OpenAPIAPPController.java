package com.qinghua.website.api.controller;

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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/app")
public class OpenAPIAPPController {

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

    /**
     * APP注册客户信息
     * @param customerSaveIO
     * @return
     */
    @LogAnnotation(logType = "register",logDesc = "APP注册客户信息")
    @RequestMapping("/registerCustomerAPI")
    public ResponseResult<Object> registerCustomerAPI(@Validated @RequestBody CustomerSaveIO customerSaveIO){
        CustomerInfoDTO save = BeanToolsUtil.copyOrReturnNull(customerSaveIO,CustomerInfoDTO.class);
        save.setPassword(MD5Util.toMD5String(save.getPassword()));
        save.setMobile(Sm4Utils.encrypt(save.getMobile()));
        save.setIdCard(Sm4Utils.encrypt(save.getIdCard()));
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
    @RequestMapping("/getTokenAPI")
    public ResponseResult<Object> getTokenAPI(@Validated @RequestBody APPLoginIO loginIO, HttpServletRequest request){

        //先将前端传进来的RSA密码进行解密
        String password = RSACryptoHelper.decrypt(loginIO.getPassword());
        //将密码转为MD5
        String pwd = StringUtils.lowerCase(MD5Util.toMD5String(password));
        //根据账号密码生成TOKEN
        String token = TokenTools.genToken(loginIO.getCustomerName(),pwd);

        log.info("APP登录成功,返回token:{}",token);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",token);
        return ResponseResult.success(map);
    }

    /**
     * 根据ID查询客户信息
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询客户信息API")
    @RequestMapping("/queryCustomerByIdAPI")
    public ResponseResult<Object> queryCustomerByIdAPI(@RequestParam("id") Long id){
        CustomerInfoDTO customer = customerInfoService.getCustomerInfoById(id);
        CustomerInfoVO vo = BeanToolsUtil.copyOrReturnNull(customer, CustomerInfoVO.class);
        vo.setMobile(Sm4Utils.decrypt(vo.getMobile()));
        vo.setIdCard(Sm4Utils.decrypt(vo.getIdCard()));
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

            //根据商户信息查询到店铺信息，通过所属关系关联
            List<ShopsInfoDTO> shopsList = shopsInfoService.getShopsInfoByMerchantId(merchant == null ? 0L :merchant.getId());
            List<ShopsInfoVO> shopsInfoVOList = BeanToolsUtil.copyList(shopsList,ShopsInfoVO.class);

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
        merchantInfoService.updateMerchantInfoById(merchant);
        return ResponseResult.success();
    }

    /**
     * 分页查询模板信息列表API
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询模板信息列表API")
    @RequestMapping("/queryTemplateListPageAPI")
    public ResponseResult<Object> queryTemplateListPageAPI(@Validated @RequestBody TemplateQueryAPIIO templateQueryIO){
        TemplateDTO template = BeanToolsUtil.copyOrReturnNull(templateQueryIO,TemplateDTO.class);
        PageInfo<TemplateDTO> resList = templateService.queryTemplateListPageAPI(template);
        List<TemplateVO> templateVOS = BeanToolsUtil.copyList(resList.getList(),TemplateVO.class);
        PageListVO<TemplateVO> result = new PageListVO<>();
        result.setList(templateVOS);
        result.setTotal(resList.getTotal());
        return ResponseResult.success(result);
    }

    /**
     * 根据ID查询模板详情
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询模板详情")
    @RequestMapping("/queryTemplateByIdAPI")
    public ResponseResult<Object> queryTemplateByIdAPI(@RequestParam("id") Long id){
        TemplateDTO template = templateService.getTemplateByIdAPI(id);
        TemplateVO templateVO = BeanToolsUtil.copyOrReturnNull(template,TemplateVO.class);
        return ResponseResult.success(templateVO);
    }

    /**
     * APP上传店招信息
     * @param logoInfoSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "APP上传店招信息API")
    @RequestMapping("/saveLogoInfoAPI")
    public ResponseResult<Object> saveLogoInfoAPI(@Validated @RequestBody LogoInfoSaveIO logoInfoSaveIO){
        return null;
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

}
