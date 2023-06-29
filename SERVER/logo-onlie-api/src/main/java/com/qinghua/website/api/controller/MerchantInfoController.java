package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.MerchantInfoVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.MerchantInfoDTO;
import com.qinghua.website.server.service.MerchantInfoService;
import com.qinghua.website.server.utils.Sm4Utils;
import lombok.extern.slf4j.Slf4j;
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
    @RequestMapping(value = "/getMerchantInfoListByPage",method = RequestMethod.GET)
    public ResponseResult<Object> getMerchantInfoListByPage(@Validated @RequestBody MerchantQueryIO merchantQueryIO){
        MerchantInfoDTO merchantInfoDTO =  BeanToolsUtil.copyOrReturnNull(merchantQueryIO, MerchantInfoDTO.class);
        PageInfo<MerchantInfoDTO> pageList = merchantInfoService.getMerchantInfoListByPage(merchantInfoDTO);
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
    @RequestMapping(value = "/getMerchantInfoById",method = RequestMethod.GET)
    public ResponseResult<Object> getMerchantInfoById(@RequestParam("id") Long id){
        MerchantInfoDTO merchantInfoDTO = merchantInfoService.getMerchantInfoById(id);
        MerchantInfoVO merchantInfoVO = BeanToolsUtil.copyOrReturnNull(merchantInfoDTO,MerchantInfoVO.class);
        return ResponseResult.success(merchantInfoVO);
    }

    /**
     * 新增商户信息
     * @param bean
     * @return
     */
    @RequestMapping(value = "/saveMerchantInfo",method = RequestMethod.POST)
    public ResponseResult<Object> saveMerchantInfo(@Validated @RequestBody MerchantInfoSaveIO bean){
        MerchantInfoDTO merchantInfoDTO =  BeanToolsUtil.copyOrReturnNull(bean, MerchantInfoDTO.class);
        merchantInfoDTO.setPhone(Sm4Utils.encrypt(merchantInfoDTO.getPhone()));
        merchantInfoDTO.setIdCard(Sm4Utils.encrypt(merchantInfoDTO.getIdCard()));
        merchantInfoService.saveMerchantInfo(merchantInfoDTO);
        return ResponseResult.success();
    }

    /**
     * 修改商户信息
     * @param bean
     * @return
     */
    @RequestMapping(value = "/updateMerchantInfoById",method = RequestMethod.POST)
    public ResponseResult<Object> updateMerchantInfoById(@Validated @RequestBody MerchantInfoUpdateIO bean){
        MerchantInfoDTO merchantInfoDTO = BeanToolsUtil.copyOrReturnNull(bean, MerchantInfoDTO.class);
        merchantInfoDTO.setPhone(Sm4Utils.encrypt(merchantInfoDTO.getPhone()));
        merchantInfoDTO.setIdCard(Sm4Utils.encrypt(merchantInfoDTO.getIdCard()));
        merchantInfoService.updateMerchantInfoById(merchantInfoDTO);
        return ResponseResult.success();
    }

    /**
     * 删除商户信息
     * @param req
     * @return
     */
    @RequestMapping(value="/deleteMerchantInfoById", method= RequestMethod.POST)
    public ResponseResult<Object> deleteMerchantInfoById(@Valid @RequestBody IdIO req) {
        merchantInfoService.deleteMerchantInfoById(req.getId());
        return ResponseResult.success();
    }

}
