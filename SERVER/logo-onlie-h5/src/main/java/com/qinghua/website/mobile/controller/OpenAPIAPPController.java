package com.qinghua.website.mobile.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.hazelcast.util.MD5Util;
import com.qinghua.website.mobile.annotation.LogAnnotation;
import com.qinghua.website.mobile.common.TokenTools;
import com.qinghua.website.mobile.controller.io.*;
import com.qinghua.website.mobile.controller.vo.*;
import com.qinghua.website.mobile.utils.BeanToolsUtil;
import com.qinghua.website.mobile.utils.H5OSSHttpToolsUtils;
import com.qinghua.website.mobile.utils.HttpClientUtils;
import com.qinghua.website.mobile.utils.ImgUtils;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/app")
public class OpenAPIAPPController {

    @Value("${uploadPath.savePath}")
    private String savePath;   //图标物理存储路径

    @Value("${uploadPath.urlPath}")
    private String urlPath;   //图标映射路径

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

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

    @Value("${zlb.config.getUser}")
    private String ZLB_GET_USER_URL;

    /**
     * APP注册客户信息
     * @param customerSaveIO
     * @return
     */
    @LogAnnotation(logType = "register",logDesc = "APP注册客户信息")
    @RequestMapping("/registerCustomerAPI")
    public ResponseResult<Object> registerCustomerAPI(@Validated @RequestBody CustomerSaveIO customerSaveIO){
        CustomerInfoDTO save = BeanToolsUtil.copyOrReturnNull(customerSaveIO, CustomerInfoDTO.class);

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
     * 浙里办用户登录
     * @param accessToken
     * @param request
     * @return
     */
    @LogAnnotation(logType = "login",logDesc = "浙里办用户登录APP获取TOKEN")
    @RequestMapping(value = "/getZLBTokenAPI",method = RequestMethod.GET)
    public ResponseResult<Object> zlbLogin(@RequestParam("accessToken") String accessToken, HttpServletRequest request){
        if(null == ZLB_GET_USER_URL){
            return ResponseResult.error("未正确配置浙里办获取用户信息地址!");
        }
        log.info("accessToken:"+accessToken);
        if(null == accessToken){
            return ResponseResult.error("未正确传入AccessToken参数!");
        }

        String resData = HttpClientUtils.doGet(ZLB_GET_USER_URL+"?accesstoken=" + accessToken);
        log.info("resData:"+resData);
        if(null != resData){

            JSONObject obj = JSON.parseObject(resData);
            String code = obj.get("code").toString();
            if(null != code && "0".equals(code)){

                String data = obj.get("data").toString();
                if(null != data){
                    JSONObject dataObj = JSONObject.parseObject(data);

                    //客户名称
                    String customerName = dataObj.get("userName").toString();
                    //手机号
                    String mobile = dataObj.get("telephone").toString();
                    //身份证号
                    String idCard = dataObj.get("idCard").toString();
                    //昵称
                    String nickName = dataObj.get("userNo").toString();

                    String gender = "0";
                    if(null != dataObj.get("sex")){
                        String genderZLB = dataObj.get("sex").toString();
                        if (null != genderZLB) {
                            if ("1".equals(genderZLB)) {
                                gender = "2";
                            } else if ("2".equals(genderZLB)) {
                                gender = "1";
                            }
                        }
                    }

                    String password = idCard;

                    log.info("浙里办调试信息：customerName：" + customerName + ",手机号：" + mobile + ", 身份证号：" + idCard+ ",昵称：" + nickName + ", 性别：" + gender);

                    //先根据手机号查询是否已注册。
                    CustomerInfoDTO resCustInfo = customerInfoService.getCustomerByCustomerName(customerName);
                    if(null == resCustInfo){
                        //执行注册操作。
                        CustomerInfoDTO customer = new CustomerInfoDTO();
                        if(null != password){
                            customer.setPassword(MD5Util.toMD5String(password).toLowerCase());
                        }
                        if(null != mobile){
                            customer.setMobile(Sm4Utils.encrypt(mobile));
                        }
                        if (null != idCard){
                            customer.setIdCard(Sm4Utils.encrypt(idCard));
                        }
                        customer.setCustomerName(customerName);
                        customer.setNickName(nickName);
                        customer.setGender(gender);

                        customerInfoService.saveCustomerInfo(customer);

                        //执行注册商户
                        MerchantInfoDTO merchant = new MerchantInfoDTO();
                        if (null != idCard) {
                            merchant.setIdCard(Sm4Utils.encrypt(idCard));
                        }
                        if(null != mobile){
                            merchant.setPhone(Sm4Utils.encrypt(mobile));
                        }
                        merchant.setGender(gender);
                        merchant.setMerchantName(customerName);
                        merchant.setMerchantStatus("2");

                        merchantInfoService.saveMerchantInfo(merchant);

                    }

                    String pwd = StringUtils.lowerCase(MD5Util.toMD5String(password));
                    //执行登录操作
                    //先到数据库校验数据合法性，若不合法则不生成token，方便前端页面处理
                    CustomerInfoDTO checkDTO = customerInfoService.checkCustomerByPWDAndAccount(customerName,pwd);
                    if(null != checkDTO) {
                        //根据账号密码生成TOKEN
                        String token = TokenTools.genToken(customerName, pwd);
                        log.info("ZLB 用户APP登录成功,返回token:{}", token);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("token", token);

                        //添加用户信息返回
                        CustomerInfoVO vo = BeanToolsUtil.copyOrReturnNull(checkDTO, CustomerInfoVO.class);
                        if (null != vo && null != vo.getMobile()) {
                            vo.setMobile(Sm4Utils.decrypt(vo.getMobile()));
                        }
                        if (null != vo && null != vo.getIdCard()) {
                            vo.setIdCard(Sm4Utils.decrypt(vo.getIdCard()));
                        }
                        map.put("customerInfo", vo);

                        return ResponseResult.success(map);
                    }else{
                        log.error("数据库中未查询到当前浙里办用户对应的数据信息。用户名称为：" + customerName + ",密码为：" + pwd);
                        return ResponseResult.error("当前用户未创建绑定关系，请检查!");
                    }
                }else{
                    log.error("浙里办获取用户数据信息返回为空!");
                    return ResponseResult.error("浙里办获取用户数据信息返回为空!");
                }
            }else{
                log.error("浙里办用户接口返回状态异常!登录失败!");
                return ResponseResult.error("浙里办用户接口返回状态异常!登录失败!");
            }
        }else{
            log.error("根据浙里办获取用户信息失败!登录失败!");
            return ResponseResult.error("根据浙里办获取用户信息失败!登录失败!");
        }

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
     * 用户登录APP获取TOKEN
     * @param loginIO
     * @param request
     * @return
     */
    @LogAnnotation(logType = "login",logDesc = "用户登录APP获取TOKEN")
    @RequestMapping(value = "/getTokenTimestampAPI",method = RequestMethod.POST)
    public ResponseResult<Object> getTokenTimestampAPI(@Validated  @RequestBody APPQLYGLoginIO loginIO, HttpServletRequest request){

        //先将前端传进来的RSA密码进行解密
        String timestamp = RSACryptoHelper.decrypt(loginIO.getSign());

        //根据账号密码生成TOKEN
        String token = TokenTools.genTokenTimestamp(timestamp);
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
     * 根据用户名称查询用户信息(OSS)
     * @param customerName
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据用户名称查询用户信息(OSS)")
    @RequestMapping("/getCustomerInfoByUserNameAPIOSS")
    public ResponseResult<Object> getCustomerInfoByUserNameAPIOSS(@RequestParam("customerName")String customerName){

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

        //开始身份证Base64压缩
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByPhotoFront() != null){
            shopsInfoDTO.setHandledByPhotoFrontCompress(ImgUtils.resizeImageTo40K(shopsInfoDTO.getHandledByPhotoFront()));
        }
        if (null != shopsInfoDTO && shopsInfoDTO.getHandledByPhotoOpposite() != null){
            shopsInfoDTO.setHandledByPhotoOppositeCompress(ImgUtils.resizeImageTo40K(shopsInfoDTO.getHandledByPhotoOpposite()));
        }

        //转换类型
        shopsInfoDTO.setLogoWidth(Double.parseDouble(shopsInfoSaveIO.getLogoWidth()+""));
        shopsInfoDTO.setLogoHeight(Double.parseDouble(shopsInfoSaveIO.getLogoHeight()+""));

        List<ShopsAttachmentDTO> list = BeanToolsUtil.copyList(shopsInfoSaveIO.getList(), ShopsAttachmentDTO.class);
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

        //根据商铺审核状态来判断是否允许修改，审核通过的均不允许修改
        ShopsInfoDTO checkRes = shopsInfoService.getShopsInfoById(shopsInfoDTO.getId());
        if(null != checkRes && checkRes.getIsFilings().equals("2")){
            return ResponseResult.error("已备案成功的信息不允许修改，请联系管理员处理。");
        }

        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByPhone() != null){
            shopsInfoDTO.setHandledByPhone(Sm4Utils.encrypt(shopsInfoDTO.getHandledByPhone()));
        }
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByIdCard() != null) {
            shopsInfoDTO.setHandledByIdCard(Sm4Utils.encrypt(shopsInfoDTO.getHandledByIdCard()));
        }

        //开始身份证Base64压缩,
        if(null != shopsInfoDTO && shopsInfoDTO.getHandledByPhotoFront() != null){
            //修改与新增不同，修改时切记不能将缩略图传过来，要传原图。提醒前端注意。
            shopsInfoDTO.setHandledByPhotoFrontCompress(ImgUtils.resizeImageTo40K(shopsInfoDTO.getHandledByPhotoFront()));
        }
        if (null != shopsInfoDTO && shopsInfoDTO.getHandledByPhotoOpposite() != null){
            shopsInfoDTO.setHandledByPhotoOppositeCompress(ImgUtils.resizeImageTo40K(shopsInfoDTO.getHandledByPhotoOpposite()));
        }

        //转换类型
        shopsInfoDTO.setLogoWidth(Double.parseDouble(shopsInfoUpdateIO.getLogoWidth()+""));
        shopsInfoDTO.setLogoHeight(Double.parseDouble(shopsInfoUpdateIO.getLogoHeight()+""));

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
    public ResponseResult<Object> updateShopsFilingsStatusAPI(@Validated @RequestBody ShopsInfoStatusAPIIO shopsInfoStatusIO){
        ShopsInfoDTO bean = BeanToolsUtil.copyOrReturnNull(shopsInfoStatusIO,ShopsInfoDTO.class);
        shopsInfoService.updateShopsFilingsStatusAPI(bean);
        return ResponseResult.success();
    }

    /**
     * APP 根据ID查询身份证原图信息
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 根据ID查询身份证原图信息")
    @RequestMapping(value = "/getShopsIdCardByIdAPI",method = RequestMethod.GET)
    public ResponseResult<Object> getShopsIdCardByIdAPI(@RequestParam("id") Long id){
        Preconditions.checkNotNull(id,"参数：id,不能为空");
        ShopsInfoDTO res = shopsInfoService.getShopsIdCardByIdAPI(id);
        ShopsInfoIdCardPhotoVO vo = BeanToolsUtil.copyOrReturnNull(res, ShopsInfoIdCardPhotoVO.class);
        return ResponseResult.success(vo);
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

        if(null != res.getList() && res.getList().size() > 0){
            for (ShopsAttachmentDTO item: res.getList()) {
                String relativeFileName = item.getAttachmentPath()  + "/" +  item.getAttachmentName() ;
                item.setUrlPath(urlPath+"shops/" + relativeFileName);
                if(null != item.getCompressFlag() && "1".equals(item.getCompressFlag())){
                    item.setCompressUrlPath(urlPath+"shops/" + item.getAttachmentPath() + "/" + item.getAttachmentName().substring(0,item.getAttachmentName().lastIndexOf("."))+"_COMPRESS"+"."+item.getAttachmentName().substring(item.getAttachmentName().lastIndexOf(".")+1));
                }
            }
        }

        ShopsInfoAPIVO vo = BeanToolsUtil.copyOrReturnNull(res,ShopsInfoAPIVO.class);
        if(null != vo && null != vo.getHandledByPhone()){
            vo.setHandledByPhone(Sm4Utils.decrypt(vo.getHandledByPhone()));
        }
        if(null != vo && null != vo.getHandledByIdCard()){
            vo.setHandledByIdCard(Sm4Utils.decrypt(vo.getHandledByIdCard()));
        }

        List<ShopsAttachmentVO> voList = new ArrayList<>();
        if(null != res.getList() && res.getList().size() > 0){
            voList = BeanToolsUtil.copyList(res.getList(),ShopsAttachmentVO.class);
            vo.setList(voList);
        }

        return ResponseResult.success(vo);
    }

    /**
     * APP 根据商铺Id获取商铺详情信API(OSS)
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 根据商铺Id获取商铺详情信API(OSS)")
    @RequestMapping(value = "/getShopsInfoByIdAPIOSS",method = RequestMethod.GET)
    public ResponseResult<Object> getShopsInfoByIdAPIOSS(@RequestParam("shopsId")Long shopsId){
        Preconditions.checkNotNull(shopsId,"参数：shopsId,不能为空");
        ShopsInfoDTO res = shopsInfoService.getShopsInfoByIdAPI(shopsId);

        if(null != res){
            ShopsInfoAPIVO vo = BeanToolsUtil.copyOrReturnNull(res,ShopsInfoAPIVO.class);
            if(null != vo && null != vo.getHandledByPhone()){
                vo.setHandledByPhone(Sm4Utils.decrypt(vo.getHandledByPhone()));
            }
            if(null != vo && null != vo.getHandledByIdCard()){
                vo.setHandledByIdCard(Sm4Utils.decrypt(vo.getHandledByIdCard()));
            }

            List<ShopsAttachmentVO> voList = new ArrayList<>();
            if(null != res.getList() && res.getList().size() > 0){
                voList = BeanToolsUtil.copyList(res.getList(),ShopsAttachmentVO.class);
                vo.setList(voList);
            }

            return ResponseResult.success(vo);
        }else{
            throw new BizException("未查询到相关店铺信息!",SysConstant.SYSTEM_ERROR_400.getCode());
        }

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
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            boolean mkdirs = new File( savePath + "logo/" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;
            String newCompressFileName = fileId + "_COMPRESS." + fileType;

            String relativeFileName = frontPath + "/" + newFileName;
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
            fileVO.setCompressUrlPath(urlPath+ "logo/" + compressRelativeFileName);
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
     * APP上传店招信息(oss)有商铺
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP上传店招信息API(oss)有商铺")
    @RequestMapping(value = "/saveLogoInfoAPIOSS", method = RequestMethod.POST)
    public ResponseResult<Object> saveLogoInfoAPIOSS(@RequestPart("file") MultipartFile multipartFile,  Long shopsId, Long merchantId ,HttpServletRequest request) {

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String newFileName = fileId + "." + fileType;
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());


            String fileDir = "upload/logo/" + frontPath;
            String objectName = fileDir + "/" + newFileName;
            //上传到OSS
            String resUrl = H5OSSHttpToolsUtils.uploadImg2Oss(fileDir,newFileName,multipartFile)
                    .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");

            if(null != shopsId && null != merchantId){
                LogoInfoDTO logoInfoDTO = new LogoInfoDTO();
                logoInfoDTO.setShopsId(shopsId);
                logoInfoDTO.setMerchantId(merchantId);
                logoInfoDTO.setLogoName(fileName);
                logoInfoDTO.setLogoFileName(newFileName);
                logoInfoDTO.setLogoFilePath(objectName);
                logoInfoDTO.setUrlPath(resUrl);

                //根据shopsId判断数据是新增还是更新
                LogoInfoDTO res = logoInfoService.getLogoInfoByShopsIdAPI(shopsId);
                if(null != res){
                    logoInfoDTO.setId(res.getId());
                    logoInfoService.updateLogoInfoById(logoInfoDTO);
                }else{
                    logoInfoService.saveLogoInfo(logoInfoDTO);
                }
            }


            FileOSSVO fileVO = new FileOSSVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(objectName);
            fileVO.setAttachmentName(newFileName);
            fileVO.setUrlPath(resUrl);
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
     * APP上传店招信息(oss)无商铺
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP上传店招信息API(oss)无商铺")
    @RequestMapping(value = "/saveLogoInfoAPIOSSQLYG", method = RequestMethod.POST)
    public ResponseResult<Object> saveLogoInfoAPIOSSQLYG(@RequestPart("file") MultipartFile multipartFile, HttpServletRequest request) {

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String newFileName = fileId + "." + fileType;
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());


            String fileDir = "upload/logo/" + frontPath;
            //上传到OSS
            String resUrl = H5OSSHttpToolsUtils.uploadImg2Oss(fileDir,newFileName,multipartFile)
                    .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");

            FileOSSVO fileVO = new FileOSSVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(fileDir+"/"+newFileName);
            fileVO.setAttachmentName(newFileName);
            fileVO.setUrlPath(resUrl);
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
     * APP 根据ObjectName获取OSS_URL路径 API
     * @param objectName
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 根据ObjectName获取OSS_URL路径 API")
    @RequestMapping(value = "/getFileUrlByObjName", method = RequestMethod.GET)
    public ResponseResult<Object> getFileUrlByObjName(@RequestParam("objectName") String objectName){
        Preconditions.checkNotNull(objectName,"objectName 不能为空!");
        String url = H5OSSHttpToolsUtils.getUrl(objectName);
        OSSResVO resVO = new OSSResVO();
        resVO.setFilePath(objectName);
        resVO.setFileUrl(url);
        return ResponseResult.success(resVO);
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
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            boolean mkdirs = new File( savePath + "logo/" + frontPath).mkdirs();

            String newFileName = fileId + "." + fileType;
            String newCompressFileName = fileId + "_COMPRESS." + fileType;

            String relativeFileName = frontPath + "/" + newFileName;
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
            fileVO.setCompressUrlPath(urlPath+ "logo/" + compressRelativeFileName);
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
     * APP 上传店招Base64文件信息API(oss)
     * @param logoInfoBase64IO
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 上传店招Base64文件信息API(oss)")
    @RequestMapping(value = "/saveLogoInfoBase64APIOSS", method = RequestMethod.POST)
    public ResponseResult<Object> saveLogoInfoBase64APIOSS(@RequestBody LogoInfoBase64IO logoInfoBase64IO , HttpServletRequest request) {

        CommonsMultipartFile multipartFile = base64toMultipartFile(logoInfoBase64IO.getBase64());

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String newFileName = fileId + "." + fileType;
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

            String fileDir = "upload/logo/" + frontPath;
            String objectName = fileDir + "/" + newFileName;
            //上传到OSS
            String resUrl = H5OSSHttpToolsUtils.uploadImg2Oss(fileDir,newFileName,multipartFile)
                    .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");

            if(null != logoInfoBase64IO.getShopsId() && null != logoInfoBase64IO.getMerchantId()){
                LogoInfoDTO logoInfoDTO = new LogoInfoDTO();
                logoInfoDTO.setShopsId(logoInfoBase64IO.getShopsId());
                logoInfoDTO.setMerchantId(logoInfoBase64IO.getMerchantId());
                logoInfoDTO.setLogoName(fileName);
                logoInfoDTO.setLogoFileName(newFileName);
                logoInfoDTO.setLogoFilePath(objectName);
                logoInfoDTO.setUrlPath(resUrl);

                //根据shopsId判断数据是新增还是更新
                LogoInfoDTO res = logoInfoService.getLogoInfoByShopsIdAPI(logoInfoBase64IO.getShopsId());
                if(null != res){
                    logoInfoDTO.setId(res.getId());
                    logoInfoService.updateLogoInfoById(logoInfoDTO);
                }else{
                    logoInfoService.saveLogoInfo(logoInfoDTO);
                }
            }

            FileOSSVO fileVO = new FileOSSVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(fileDir+"/"+newFileName);
            fileVO.setAttachmentName(newFileName);
            fileVO.setUrlPath(resUrl);
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
        logoInfoVOList.forEach(item->{
            item.setUrlPath(urlPath+"logo/"+item.getLogoFilePath()+"/"+item.getLogoFileName());
            if(null != item.getCompressFlag() && "1".equals(item.getCompressFlag())){
                item.setCompressUrlPath(urlPath+"logo/"+item.getLogoFilePath()+"/"+item.getLogoFileName().substring(0,item.getLogoFileName().lastIndexOf("."))+"_COMPRESS"+"."+item.getLogoFileName().substring(item.getLogoFileName().lastIndexOf(".")+1));
            }
        });
        PageListVO<LogoInfoVO> resp = new PageListVO<>();
        resp.setList(logoInfoVOList);
        resp.setTotal(pageList.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * APP 分页查询查询店招图片列表API(oss)
     * @param logoQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 分页查询查询店招图片列表API(OSS)")
    @RequestMapping(value = "/getLogoInfoListPageByNameAPIOSS")
    public ResponseResult<Object> getLogoInfoListPageByNameAPIOSS(@Validated LogoQueryIO logoQueryIO){
        LogoInfoDTO queryDTO = BeanToolsUtil.copyOrReturnNull(logoQueryIO,LogoInfoDTO.class);
        PageInfo<LogoInfoDTO> pageList =  logoInfoService.getLogoInfoListByPage(queryDTO);
        List<LogoInfoVO> logoInfoVOList =  BeanToolsUtil.copyAsList(pageList.getList(),LogoInfoVO.class);
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
            if(null != res.getCompressFlag() && "1".equals(res.getCompressFlag())){
                logoInfoVO.setCompressUrlPath(urlPath+"logo/"+logoInfoVO.getLogoFilePath() + "/" + logoInfoVO.getLogoFileName().substring(0,logoInfoVO.getLogoFileName().lastIndexOf("."))+"_COMPRESS"+"."+logoInfoVO.getLogoFileName().substring(logoInfoVO.getLogoFileName().lastIndexOf(".")+1));
            }
            return ResponseResult.success(logoInfoVO);
        }else{
           return ResponseResult.success("未查询到数据");
        }
    }

    /**
     * APP 根据商铺ID获取店招信息API(OSS)
     * @param shopsId
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 根据商铺ID获取店招信息API(OSS)")
    @RequestMapping(value = "/getLogoInfoByShopsIdAPIOSS",method = RequestMethod.GET)
    public ResponseResult<Object> getLogoInfoByShopsIdAPIOSS(@RequestParam("shopsId") Long shopsId){
        LogoInfoDTO res = logoInfoService.getLogoInfoByShopsIdAPI(shopsId);
        if(null != res){
            LogoInfoVO logoInfoVO = BeanToolsUtil.copyOrReturnNull(res,LogoInfoVO.class);
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
     * 分页查询文章列表API
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据栏目ID分頁查询文章列表 API")
    @RequestMapping("/getContentByChannelIdAPI")
    public ResponseResult<Object> getContentByChannelIdAPI(@Validated ContentQueryIO bean){
        ContentDTO contentDTO = BeanToolsUtil.copyOrReturnNull(bean,ContentDTO.class);
        PageInfo<ContentDTO> pageList = contentService.getContentByChannelIdAPI(contentDTO);
        List<ContentVO> contentVOList = new ArrayList<>();
        if(null != pageList && 0 != pageList.getList().size()){
            for(int i = 0 ;i<pageList.getList().size();i++){
                if(null != pageList.getList().get(i).getContentExt()){
                    contentVOList.add(BeanToolsUtil.copyOrReturnNull(pageList.getList().get(i),ContentVO.class));
                }
            }
            if(null != contentVOList && 0 != contentVOList.size()){
                PageListVO<ContentVO> result = new PageListVO<>();
                result.setList(contentVOList);
                result.setTotal(pageList.getTotal());
                return ResponseResult.success(result);
            }else{
                return ResponseResult.success("该栏目下没有已发布且已到发布时间的文章!");
            }
        }else {
            return ResponseResult.success("该栏目下没有已发布且已到发布时间的文章!");
        }
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
     * APP 分頁查询OSS素材信息API
     * @param materialQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "APP 分頁查询OSS素材信息API")
    @RequestMapping("/getMaterialListByPageAPIOSS")
    public ResponseResult<Object> getMaterialListByPageAPIOSS(@Validated MaterialQueryIO materialQueryIO){
        MaterialDTO queryDTO = BeanToolsUtil.copyOrReturnNull(materialQueryIO,MaterialDTO.class);
        PageInfo<MaterialDTO> pageList =  materialService.getMaterialListByPage(queryDTO);
        List<MaterialVO> materialVOList =  BeanToolsUtil.copyAsList(pageList.getList(),MaterialVO.class);
        PageListVO<MaterialVO> resp = new PageListVO<>();
        resp.setList(materialVOList);
        resp.setTotal(pageList.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * 上传素材附件 API
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 上传素材附件API")
    @RequestMapping("/uploadMaterialAttachmentAPI")
    public ResponseResult<Object> uploadMaterialAttachmentAPI(@RequestPart("file")  MultipartFile multipartFile, HttpServletRequest request) {

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
     * 上传素材附件 API(OSS)
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 上传素材附件API(OSS)")
    @RequestMapping("/uploadMaterialAttachmentAPIOSS")
    public ResponseResult<Object> uploadMaterialAttachmentAPIOSS(@RequestPart("file")  MultipartFile multipartFile, HttpServletRequest request) {

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

            String newFileName = fileId + "." + fileType;

            String fileDir = "upload/material/" + frontPath;
            String objectName = fileDir + "/" + newFileName;

            //上传到OSS
            String resUrl = H5OSSHttpToolsUtils.uploadImg2Oss(fileDir,newFileName,multipartFile)
                    .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");

            //更新素材
            MaterialDTO materialDTO = new MaterialDTO();
            materialDTO.setName(fileName);
            materialDTO.setFileName(newFileName);
            if(fileType.equals("JPEG") || fileType.equals("JPG") || fileType.equals("SVG") || fileType.equals("PNG")){
                materialDTO.setFileType("1");
            }else{
                materialDTO.setFileType("2");
            }
            materialDTO.setFilePath(objectName);
            materialDTO.setUrlPath(resUrl);

            materialService.saveMaterial(materialDTO);

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(objectName);
            fileVO.setAttachmentName(newFileName);
            fileVO.setAttachmentType(materialDTO.getFileType());
            fileVO.setUrlPath(resUrl);
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
        }
    }

    /**
     * APP 上传素材附件Base64 API(OSS)
     * @param base64
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 上传素材附件Base64 API(OSS)")
    @RequestMapping(value = "/uploadMaterialAttachmentBase64APIOSS",method = RequestMethod.POST)
    public ResponseResult<Object> uploadMaterialAttachmentBase64APIOSS(@RequestBody String base64, HttpServletRequest request) {

        Preconditions.checkNotNull(base64,"Base64String 不能为空");

        CommonsMultipartFile multipartFile = base64toMultipartFile(base64);

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

            String newFileName = fileId + "." + fileType;

            String fileDir = "upload/material/" + frontPath;
            String objectName = fileDir + "/" + newFileName;

            //上传到OSS
            String resUrl = H5OSSHttpToolsUtils.uploadImg2Oss(fileDir,newFileName,multipartFile)
                    .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");

            //更新素材
            MaterialDTO materialDTO = new MaterialDTO();
            materialDTO.setName(fileName);
            materialDTO.setFileName(newFileName);
            if(fileType.equals("JPEG") || fileType.equals("JPG") || fileType.equals("SVG") || fileType.equals("PNG")){
                materialDTO.setFileType("1");
            }else{
                materialDTO.setFileType("2");
            }
            materialDTO.setFilePath(objectName);
            materialDTO.setUrlPath(resUrl);

            materialService.saveMaterial(materialDTO);

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(objectName);
            fileVO.setAttachmentName(newFileName);
            fileVO.setAttachmentType(materialDTO.getFileType());
            fileVO.setUrlPath(resUrl);
            return ResponseResult.success(fileVO);
        } catch (Exception exception) {
            throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
        }
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
            if(exception instanceof BizException){
                throw new BizException(exception.getMessage(),SysConstant.SYSTEM_ERROR_500.getCode());
            }else {
                throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
            }
        }
    }

    /**
     * APP 上传商铺附件(OSS)
     * @param multipartFile
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 上传商铺附件API(OSS)")
    @RequestMapping(value = "/uploadShopsAttachmentAPIOSS", method = RequestMethod.POST)
    public ResponseResult<Object> uploadShopsAttachmentAPIOSS(@RequestPart("file")  MultipartFile multipartFile,Long shopsId,String attachmentType, HttpServletRequest request) {

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);

            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String newFileName = fileId + "." + fileType;

            String fileDir = "upload/shops/" + frontPath;
            String objectName = fileDir + "/" + newFileName;

            //上传到OSS
            String resUrl = H5OSSHttpToolsUtils.uploadImg2Oss(fileDir,newFileName,multipartFile)
                    .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");


            if(null != shopsId){
                //更新数据库关系
                ShopsAttachmentDTO shopsAttachmentDTO = new ShopsAttachmentDTO();
                shopsAttachmentDTO.setShopsId(shopsId);
                shopsAttachmentDTO.setFileName(fileName);
                shopsAttachmentDTO.setAttachmentName(newFileName);
                shopsAttachmentDTO.setAttachmentPath(objectName);
                shopsAttachmentDTO.setAttachmentType(attachmentType);
                shopsAttachmentDTO.setUrlPath(resUrl);

                List<ShopsAttachmentDTO> list = new ArrayList<>();
                list.add(shopsAttachmentDTO);

                shopsInfoService.saveShopsAttachments(list);
            }

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(objectName);
            fileVO.setAttachmentName(newFileName);
            fileVO.setAttachmentType(attachmentType);
            fileVO.setUrlPath(resUrl);

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
                String relativeFileName = attachment.getAttachmentPath() + File.separator + attachment.getAttachmentName();

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
     * APP 预览文章附件API
     * @param attachmentName
     * @param request
     * @param response
     * @return
     */
    @LogAnnotation(logType = "preview",logDesc = "APP 预览文章附件API")
    @RequestMapping(value = "/previewContentAttachment", method = RequestMethod.GET)
    public void previewContentAttachment(@RequestParam("attachmentName") String attachmentName, HttpServletRequest request, HttpServletResponse response) {

        Preconditions.checkNotNull(attachmentName,"参数：attachmentName 不能为空");

        try {
            ContentAttachmentDTO attachment = attachmentService.getAttachmentByAttachmentName(attachmentName);
            if(null != attachment && null != attachment.getAttachmentPath() && null != attachment.getAttachmentName()){
                String relativeFileName = attachment.getAttachmentPath() + File.separator + attachment.getAttachmentName();

                String fullName = savePath + "content/" + relativeFileName;
                File file = new File(fullName);
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                response.reset();
                response.setCharacterEncoding("utf-8");

                String fileType = attachment.getAttachmentName().substring(attachment.getAttachmentName().lastIndexOf(".")+1);

                if(null != attachment.getAttachmentName() && fileType.toLowerCase().equals("pdf")){
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(file.getName(), "utf-8"));
                }else if (null != attachment.getAttachmentName() && fileType.toLowerCase().equals("docx")){
                    response.setContentType("application/msword");
                    response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(file.getName(), "utf-8"));
                }else if(null != attachment.getAttachmentName() && fileType.toLowerCase().equals("txt")){
                    response.setContentType("text/plain");
                    response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(file.getName(), "utf-8"));
                }

                response.setHeader("Content-Length", String.valueOf(file.length()));
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
                byte[] bytes = new byte[1024];
                int len;
                while ((len = bufferedInputStream.read(bytes)) > 0) {
                    bufferedOutputStream.write(bytes, 0, len);
                }
                bufferedInputStream.close();
                bufferedOutputStream.flush();

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
    @RequestMapping(value = "/uploadShopsContentAttachmentBase64API", method = RequestMethod.POST)
    public ResponseResult<Object> uploadShopsContentAttachmentBase64API(@RequestBody String base64, @RequestBody Long shopsId, @RequestBody String attachmentType, HttpServletRequest request) {
        Preconditions.checkNotNull(base64,"Base64String 不能为空");

        CommonsMultipartFile multipartFile = base64toMultipartFile(base64);

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
            if(exception instanceof BizException){
                throw new BizException(exception.getMessage(),SysConstant.SYSTEM_ERROR_500.getCode());
            }else {
                throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
            }
        }

    }

    /**
     * APP 上传商铺Base64文件API(OSS)
     * @param shopsContentAttInfoIO
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 上传商铺Base64文件API(OSS)")
    @RequestMapping(value = "/uploadShopsContentAttachmentBase64APIOSS", method = RequestMethod.POST)
    public ResponseResult<Object> uploadShopsContentAttachmentBase64APIOSS(@RequestBody ShopsContentAttInfoIO shopsContentAttInfoIO, HttpServletRequest request) {

        CommonsMultipartFile multipartFile = base64toMultipartFile(shopsContentAttInfoIO.getBase64());

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

            String newFileName = fileId + "." + fileType;

            String fileDir = "upload/shops/" + frontPath;
            String objectName = fileDir + "/" + newFileName;

            //上传到OSS
            String resUrl = H5OSSHttpToolsUtils.uploadImg2Oss(fileDir,newFileName,multipartFile)
                    .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");

            if(null != shopsContentAttInfoIO.getShopsId()){
                //更新数据库关系
                ShopsAttachmentDTO shopsAttachmentDTO = new ShopsAttachmentDTO();
                shopsAttachmentDTO.setShopsId(shopsContentAttInfoIO.getShopsId());
                shopsAttachmentDTO.setFileName(fileName);
                shopsAttachmentDTO.setAttachmentName(newFileName);
                shopsAttachmentDTO.setAttachmentPath(objectName);
                shopsAttachmentDTO.setAttachmentType(shopsContentAttInfoIO.getAttachmentType());
                shopsAttachmentDTO.setUrlPath(resUrl);

                List<ShopsAttachmentDTO> list = new ArrayList<>();
                list.add(shopsAttachmentDTO);

                shopsInfoService.saveShopsAttachments(list);
            }

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(objectName);
            fileVO.setAttachmentName(newFileName);
            fileVO.setAttachmentType(shopsContentAttInfoIO.getAttachmentType());
            fileVO.setUrlPath(resUrl);

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
     * APP Excel上传(OSS)
     * @param shopsContentAttInfoIO
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP Excel上传(OSS)")
    @RequestMapping(value = "/uploadExcelBase64APIOSS", method = RequestMethod.POST)
    public ResponseResult<Object> uploadExcelBase64APIOSS(@RequestBody ShopsContentAttInfoIO shopsContentAttInfoIO, HttpServletRequest request) {

        CommonsMultipartFile multipartFile = base64toExcelFile(shopsContentAttInfoIO.getBase64());

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

            String newFileName = fileId + "." + fileType;

            String fileDir = "upload/excel/" + frontPath;
            String objectName = fileDir + "/" + newFileName;

            //上传到OSS
            String resUrl = H5OSSHttpToolsUtils.uploadImg2Oss(fileDir,newFileName,multipartFile)
                    .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(objectName);
            fileVO.setAttachmentName(newFileName);
            fileVO.setAttachmentType(shopsContentAttInfoIO.getAttachmentType());
            fileVO.setUrlPath(resUrl);

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
     * APP 测试商铺OSS上传
     * @param shopsContentAttInfoIO
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "APP 测试商铺OSS上传(OSS)")
    @RequestMapping(value = "/uploadTestBase64APIOSS", method = RequestMethod.POST)
    public ResponseResult<Object> uploadTestBase64APIOSS(@RequestBody ShopsContentAttInfoIO shopsContentAttInfoIO, HttpServletRequest request) {

        CommonsMultipartFile multipartFile = base64toMultipartFile(shopsContentAttInfoIO.getBase64());

        checkFile(multipartFile);

        try {

            String fileId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            String frontPath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

            String newFileName = fileId + "." + fileType;

            String fileDir = "upload/shops/" + frontPath;
            String objectName = fileDir + "/" + newFileName;

            //上传到OSS
            String resUrl = H5OSSHttpToolsUtils.uploadImg2Oss(fileDir,newFileName,multipartFile)
                    .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");

            FileVO fileVO = new FileVO();
            fileVO.setFileName(fileName);
            fileVO.setAttachmentPath(objectName);
            fileVO.setAttachmentName(newFileName);
            fileVO.setAttachmentType(shopsContentAttInfoIO.getAttachmentType());
            fileVO.setUrlPath(resUrl);

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
     * Base64 转 MultipartFile
     * @param base64
     * @return
     * @throws IOException
     */
    public static CommonsMultipartFile base64toExcelFile(String base64) {
        final File file = base64ToExcelFile(base64);
        DiskFileItem item = (DiskFileItem) new DiskFileItemFactory().createItem(file.getName(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", false, file.getName());
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
     * Base64 转 File
     * @param base64
     * @return
     */
    public static File base64ToExcelFile(String base64) {

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
            file = File.createTempFile("tmp", ".xlsx");
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

    /**
     * 更新数据库OSS信息
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "APP 更新OSS数据信息 API")
    @RequestMapping(value = "/updateOSSDataInit", method = RequestMethod.GET)
    public ResponseResult<Object> updateOSSDataInit() throws Exception{

        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM t_material ");
            List<MaterialDTO> resList = new ArrayList<>();

            while (rs.next()) {
                String newFilePath = "";
                String ossUrl = "";
                long id = rs.getInt("id");
                String filePath = rs.getString("file_path");
                String fileName = rs.getString("file_name");

                MaterialDTO material = new MaterialDTO();
                material.setId(id);
                material.setFilePath(filePath);
                material.setFileName(fileName);
                resList.add(material);

            }

            log.info("共查询到"+resList.size()+"条素材数据!");

            for(int i = 0;i < resList.size();i++){
                MaterialDTO dto = resList.get(i);
                //判断filePath是否是oss 的ObjectName
                if(null != dto.getFilePath() && dto.getFilePath().indexOf("upload") < 0){
                    String newFilePath = "upload/material/" + dto.getFilePath() + "/" + dto.getFileName();
                    //根据objectName 获取url
                    String ossUrl = H5OSSHttpToolsUtils.getUrl(newFilePath)
                            .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");
                    log.info("素材OSSURL< " + i + ">：" + ossUrl);
                    // 在这里编写需要更新的字段及相应的值
                    String updateSql = "UPDATE t_material SET file_path = '" + newFilePath + "',url_path = '" + ossUrl + "' WHERE id=" + dto.getId();
                    log.info("当前SQL语句为：" + updateSql);
                    stmt.executeUpdate(updateSql);
                }
            }

            log.info("素材表数据更新完成!");

            log.info("开始更新商铺表附件数据……");
            rs = stmt.executeQuery("SELECT * FROM t_shops_attachment ");
            List<ShopsAttachmentDTO> resList2 = new ArrayList<>();

            while (rs.next()) {
                String newFilePath = "";
                String ossUrl = "";
                long id = rs.getInt("id");
                String filePath = rs.getString("attachment_path");
                String fileName = rs.getString("attachment_name");

                ShopsAttachmentDTO shopsAttachmentDTO = new ShopsAttachmentDTO();
                shopsAttachmentDTO.setId(id);
                shopsAttachmentDTO.setAttachmentPath(filePath);
                shopsAttachmentDTO.setAttachmentName(fileName);
                resList2.add(shopsAttachmentDTO);

            }

            log.info("共查询到"+resList2.size()+"条商铺数据!");

            for(int i = 0;i < resList2.size();i++){
                ShopsAttachmentDTO dto = resList2.get(i);
                //判断filePath是否是oss 的ObjectName
                if(null != dto.getAttachmentPath() && dto.getAttachmentPath().indexOf("upload") < 0){
                    String newFilePath = "upload/shops/" + dto.getAttachmentPath() + "/" + dto.getAttachmentName();
                    //根据objectName 获取url
                    String ossUrl = H5OSSHttpToolsUtils.getUrl(newFilePath)
                            .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");
                    log.info("商铺OSSURL< " + i + ">：" + ossUrl);
                    // 在这里编写需要更新的字段及相应的值
                    String updateSql = "UPDATE t_shops_attachment SET attachment_path = '" + newFilePath + "',url_path = '" + ossUrl + "' WHERE id=" + dto.getId();
                    log.info("当前SQL语句为：" + updateSql);
                    stmt.executeUpdate(updateSql);
                }
            }
            log.info("商铺表数据更新完成!");

            log.info("开始更新店招表附件数据……");
            rs = stmt.executeQuery("SELECT * FROM t_logo_info ");
            List<LogoInfoDTO> resList3 = new ArrayList<>();

            while (rs.next()) {
                String newFilePath = "";
                String ossUrl = "";
                long id = rs.getInt("id");
                String filePath = rs.getString("logo_file_path");
                String fileName = rs.getString("logo_file_name");

                LogoInfoDTO logo = new LogoInfoDTO();
                logo.setId(id);
                logo.setLogoFilePath(filePath);
                logo.setLogoFileName(fileName);
                resList3.add(logo);

            }

            log.info("共查询到"+resList3.size()+"条店招数据!");

            for(int i = 0;i < resList3.size();i++){
                LogoInfoDTO dto = resList3.get(i);
                //判断filePath是否是oss 的ObjectName
                if(null != dto.getLogoFilePath() && dto.getLogoFilePath().indexOf("upload") < 0){
                    String newFilePath = "upload/logo/" + dto.getLogoFilePath() + "/" + dto.getLogoFileName();
                    //根据objectName 获取url
                    String ossUrl = H5OSSHttpToolsUtils.getUrl(newFilePath)
                            .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");
                    log.info("店招OSSURL< " + i + ">：" + ossUrl);
                    // 在这里编写需要更新的字段及相应的值
                    String updateSql = "UPDATE t_logo_info SET logo_file_path = '" + newFilePath + "',url_path = '" + ossUrl + "' WHERE id=" + dto.getId();
                    log.info("当前SQL语句为：" + updateSql);
                    stmt.executeUpdate(updateSql);
                }
            }
            log.info("店招表数据更新完成!");

            log.info("开始更新文章表附件数据……");
            rs = stmt.executeQuery("SELECT * FROM t_content_attachment ");
            List<ContentAttachmentDTO> resList4 = new ArrayList<>();

            while (rs.next()) {
                String newFilePath = "";
                String ossUrl = "";
                long id = rs.getInt("id");
                String filePath = rs.getString("attachment_path");
                String fileName = rs.getString("attachment_name");

                ContentAttachmentDTO contentAttachmentDTO = new ContentAttachmentDTO();
                contentAttachmentDTO.setId(id);
                contentAttachmentDTO.setAttachmentPath(filePath);
                contentAttachmentDTO.setAttachmentName(fileName);
                resList4.add(contentAttachmentDTO);

            }

            log.info("共查询到"+resList4.size()+"条文章附件数据!");

            for(int i = 0;i < resList4.size();i++){
                ContentAttachmentDTO dto = resList4.get(i);
                //判断filePath是否是oss 的ObjectName
                if(null != dto.getAttachmentPath() && dto.getAttachmentPath().indexOf("upload") < 0){
                    String newFilePath = "upload/content/" + dto.getAttachmentPath() + "/" + dto.getAttachmentName();
                    //根据objectName 获取url
                    String ossUrl = H5OSSHttpToolsUtils.getUrl(newFilePath)
                            .replace("http://10.253.16.56:10012/cdsdzaliossvpn1/img-save-dir/","https://img-save-dir.oss-cn-hangzhou.aliyuncs.com/");
                    log.info("文章OSSURL< " + i + ">：" + ossUrl);
                    // 在这里编写需要更新的字段及相应的值
                    String updateSql = "UPDATE t_content_attachment SET attachment_path = '" + newFilePath + "',url_path = '" + ossUrl + "' WHERE id=" + dto.getId();
                    log.info("当前SQL语句为：" + updateSql);
                    stmt.executeUpdate(updateSql);
                }
            }
            log.info("文章附件表数据更新完成!");

            log.info("数据已成功更新！");

        } catch (Exception e) {
            log.info("同步OSS数据失败!",e.getMessage());
            throw new BizException("同步更新OSS数据失败!",SysConstant.SYSTEM_ERROR_500.getCode());
        }finally {
            rs.close();
            stmt.close();
            conn.close();
        }

        return ResponseResult.success(true);
    }

}
