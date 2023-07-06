package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.ShopsInfoVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.ContentAttachmentDTO;
import com.qinghua.website.server.domain.ShopsAttachmentDTO;
import com.qinghua.website.server.domain.ShopsInfoDTO;
import com.qinghua.website.server.service.ShopsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shops-info")
public class ShopsInfoController {

    @Autowired
    private ShopsInfoService shopsInfoService;

    /**
     * 分页查询店铺信息列表
     * @param shopsInfoQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询店铺信息列表")
    @RequestMapping(value = "/getShopsInfoListByPage",method = RequestMethod.GET)
    public ResponseResult<Object> getShopsInfoListByPage(@Validated @RequestBody ShopsInfoQueryIO shopsInfoQueryIO){
        ShopsInfoDTO shopsInfoDTO =  BeanToolsUtil.copyOrReturnNull(shopsInfoQueryIO, ShopsInfoDTO.class);
        PageInfo<ShopsInfoDTO> pageList = shopsInfoService.getShopsInfoListByPage(shopsInfoDTO);
        List<ShopsInfoVO>  shopsInfoVOS = BeanToolsUtil.copyList(pageList.getList(),ShopsInfoVO.class);
        PageListVO<ShopsInfoVO> result = new PageListVO<>();
        result.setList(shopsInfoVOS);
        result.setTotal(pageList.getTotal());
        return ResponseResult.success(result);
    }

    /**
     * 根据店铺ID查询店铺信息
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据店铺ID查询店铺信息")
    @RequestMapping(value = "/getShopsInfoById",method = RequestMethod.GET)
    public ResponseResult<Object> getShopsInfoById(@RequestParam("id") Long id){
        ShopsInfoDTO shopsInfoDTO = shopsInfoService.getShopsInfoById(id);
        ShopsInfoVO shopsInfoVO = BeanToolsUtil.copyOrReturnNull(shopsInfoDTO,ShopsInfoVO.class);
        return ResponseResult.success(shopsInfoVO);
    }

    /**
     * 新增商铺信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增商铺信息")
    @RequestMapping(value = "/saveShopsInfo",method = RequestMethod.POST)
    public ResponseResult<Object> saveShopsInfo(@Validated @RequestBody ShopsInfoSaveIO bean){
        ShopsInfoDTO shopsInfoDTO =  BeanToolsUtil.copyOrReturnNull(bean, ShopsInfoDTO.class);
        List<ShopsAttachmentDTO> list = BeanToolsUtil.copyList(bean.getList(),ShopsAttachmentDTO.class);
        shopsInfoService.saveShopsInfo(shopsInfoDTO,list);
        return ResponseResult.success();
    }

    /**
     * 修改店铺信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "修改店铺信息")
    @RequestMapping(value = "/updateShopsInfoById",method = RequestMethod.POST)
    public ResponseResult<Object> updateShopsInfoById(@Validated @RequestBody ShopsInfoUpdateIO bean){
        ShopsInfoDTO shopsInfoDTO = BeanToolsUtil.copyOrReturnNull(bean, ShopsInfoDTO.class);
        List<ShopsAttachmentDTO> list = BeanToolsUtil.copyList(bean.getList(),ShopsAttachmentDTO.class);
        shopsInfoService.updateShopsInfoById(shopsInfoDTO,list);
        return ResponseResult.success();
    }

    /**
     * 删除店铺信息
     * @param req
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "删除店铺信息")
    @RequestMapping(value="/deleteShopsInfoById", method= RequestMethod.POST)
    public ResponseResult<Object> deleteShopsInfoById(@Valid @RequestBody IdIO req) {
        shopsInfoService.deleteShopsInfoById(req.getId());
        return ResponseResult.success();
    }

    /**
     * 根据附件名称删除商铺附件
     * @param attachmentName
     * @param request
     * @return
     */
    @LogAnnotation(logType = "upload",logDesc = "根据附件名称删除商铺附件")
    @RequestMapping(value = "/deleteShopsAttachment", method = RequestMethod.POST)
    public ResponseResult<Object> deleteShopsAttachment(@RequestParam("attachmentName") String attachmentName, HttpServletRequest request) {
        Preconditions.checkNotNull(attachmentName,"参数：attachmentName 不能为空");
        shopsInfoService.deleteAttachmentByName(attachmentName);
        return ResponseResult.success();
    }

}
