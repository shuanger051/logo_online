package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.MerchantInfoVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.MerchantInfoDTO;
import com.qinghua.website.server.service.MerchantInfoService;
import com.qinghua.website.server.utils.Sm4Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 商户信息
 */
@Slf4j
@RestController
@RequestMapping("/merchantInfo")
public class MerchantInfoController {

    @Autowired
    private MerchantInfoService merchantInfoService;

    /**
     * 分页查询商户列表
     * @param merchantQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询商户列表")
    @RequestMapping(value = "/getMerchantInfoListByPage",method = RequestMethod.GET)
    @RequiresPermissions("/merchantInfo/getMerchantInfoListByPage")
    public ResponseResult<Object> getMerchantInfoListByPage(@Validated MerchantQueryIO merchantQueryIO){
        MerchantInfoDTO merchantInfoDTO =  BeanToolsUtil.copyOrReturnNull(merchantQueryIO, MerchantInfoDTO.class);
        PageInfo<MerchantInfoDTO> pageList = merchantInfoService.getMerchantInfoListByPage(merchantInfoDTO);

        //解密DTO中的mobile加密串
        pageList.getList().forEach(item -> {item.setPhone(Sm4Utils.decrypt(item.getPhone()));item.setIdCard(Sm4Utils.decrypt(item.getIdCard()));});

        List<MerchantInfoVO> merchantInfoDTOList =  BeanToolsUtil.copyAsList(pageList.getList(), MerchantInfoVO.class);
        PageListVO<MerchantInfoVO> result = new PageListVO<>();
        result.setList(merchantInfoDTOList);
        result.setTotal(pageList.getTotal());
        return ResponseResult.success(result);
    }

    /**
     * 根据ID查询商户信息
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询商户信息")
    @RequestMapping(value = "/getMerchantInfoById",method = RequestMethod.GET)
    @RequiresPermissions("/merchantInfo/getMerchantInfoById")
    public ResponseResult<Object> getMerchantInfoById(@RequestParam("id") Long id){
        MerchantInfoDTO merchantInfoDTO = merchantInfoService.getMerchantInfoById(id);
        if(null != merchantInfoDTO && null != merchantInfoDTO.getPhone()) {
            merchantInfoDTO.setPhone(Sm4Utils.decrypt(merchantInfoDTO.getPhone()));
        }
        if(null != merchantInfoDTO && null != merchantInfoDTO.getIdCard()) {
            merchantInfoDTO.setIdCard(Sm4Utils.decrypt(merchantInfoDTO.getIdCard()));
        }
        MerchantInfoVO merchantInfoVO = BeanToolsUtil.copyOrReturnNull(merchantInfoDTO,MerchantInfoVO.class);
        return ResponseResult.success(merchantInfoVO);
    }

    /**
     * 新增商户信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增商户信息")
    @RequestMapping(value = "/saveMerchantInfo",method = RequestMethod.POST)
    @RequiresPermissions("/merchantInfo/saveMerchantInfo")
    public ResponseResult<Object> saveMerchantInfo(@Validated @RequestBody MerchantInfoSaveIO bean){
        MerchantInfoDTO merchantInfoDTO =  BeanToolsUtil.copyOrReturnNull(bean, MerchantInfoDTO.class);
        if(null != merchantInfoDTO && null != merchantInfoDTO.getPhone()){
            merchantInfoDTO.setPhone(Sm4Utils.encrypt(merchantInfoDTO.getPhone()));
        }
        if(null != merchantInfoDTO && null != merchantInfoDTO.getIdCard()){
            merchantInfoDTO.setIdCard(Sm4Utils.encrypt(merchantInfoDTO.getIdCard()));
        }

        merchantInfoService.saveMerchantInfo(merchantInfoDTO);
        return ResponseResult.success();
    }

    /**
     * 修改商户信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "修改商户信息")
    @RequestMapping(value = "/updateMerchantInfoById",method = RequestMethod.POST)
    @RequiresPermissions("/merchantInfo/updateMerchantInfoById")
    public ResponseResult<Object> updateMerchantInfoById(@Validated @RequestBody MerchantInfoUpdateIO bean){
        MerchantInfoDTO merchantInfoDTO = BeanToolsUtil.copyOrReturnNull(bean, MerchantInfoDTO.class);
        if(null != merchantInfoDTO && null != merchantInfoDTO.getPhone()){
            merchantInfoDTO.setPhone(Sm4Utils.encrypt(merchantInfoDTO.getPhone()));
        }
        if(null != merchantInfoDTO && null != merchantInfoDTO.getIdCard()){
            merchantInfoDTO.setIdCard(Sm4Utils.encrypt(merchantInfoDTO.getIdCard()));
        }
        merchantInfoService.updateMerchantInfoById(merchantInfoDTO);
        return ResponseResult.success();
    }

    /**
     * 删除商户信息
     * @param req
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "删除商户信息")
    @RequestMapping(value="/deleteMerchantInfoById", method= RequestMethod.POST)
    @RequiresPermissions("/merchantInfo/deleteMerchantInfoById")
    public ResponseResult<Object> deleteMerchantInfoById(@Valid @RequestBody IdIO req) {
        merchantInfoService.deleteMerchantInfoById(req.getId());
        return ResponseResult.success();
    }

}
