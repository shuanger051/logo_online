package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.ChannelQueryIO;
import com.qinghua.website.api.controller.io.ChannelSaveIO;
import com.qinghua.website.api.controller.io.ChannelUpdateIO;
import com.qinghua.website.api.controller.io.IdIO;
import com.qinghua.website.api.controller.vo.ChannelVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.ChannelDTO;
import com.qinghua.website.server.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 栏目管理
 */
@Slf4j
@RestController
@RequestMapping("/channel")
public class ChannelController{

    @Autowired
    private ChannelService channelService;

    /**
     * 分页查询栏目列表
     * @param channelQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询栏目列表")
    @RequestMapping(value = "/getChannelListByPage",method = RequestMethod.GET)
    @RequiresPermissions("/channel/getChannelListByPage")
    public ResponseResult<Object> getChannelListByPage(@Validated ChannelQueryIO channelQueryIO){
        ChannelDTO channelDTO =  BeanToolsUtil.copyOrReturnNull(channelQueryIO, ChannelDTO.class);
        PageInfo<ChannelDTO> pageList = channelService.getChannelListByPage(channelDTO);
        List<ChannelVO> channelDTOList =  BeanToolsUtil.copyAsList(pageList.getList(), ChannelVO.class);
        PageListVO<ChannelVO> result = new PageListVO<>();
        result.setList(channelDTOList);
        result.setTotal(pageList.getTotal());
        return ResponseResult.success(result);
    }

    /**
     * 根据栏目ID查询栏目信息
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据栏目ID查询栏目信息")
    @RequestMapping(value = "/getChannelByID",method = RequestMethod.GET)
    @RequiresPermissions("/channel/getChannelByID")
    public ResponseResult<Object> getChannelByID(@RequestParam("id") Long id){
        ChannelDTO channelDTO = channelService.getChannelByID(id);
        ChannelVO channelVO = BeanToolsUtil.copyOrReturnNull(channelDTO,ChannelVO.class);
        return ResponseResult.success(channelVO);
    }

    /**
     * 查询可供下拉选择的栏目信息列表
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "查询可供下拉选择的栏目信息列表")
    @RequestMapping(value = "/getChannelList",method = RequestMethod.GET)
    @RequiresPermissions("/channel/getChannelList")
    public ResponseResult<Object> getChannelList(){
        List<ChannelDTO> list = channelService.getChannelList();
        List<ChannelVO> channelVOList = BeanToolsUtil.copyList(list,ChannelVO.class);
        return ResponseResult.success(channelVOList);
    }

    /**
     * 新增栏目信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增栏目信息")
    @RequestMapping(value = "/saveChannel",method = RequestMethod.POST)
    @RequiresPermissions("/channel/saveChannel")
    public ResponseResult<Object> postChannel(@Validated @RequestBody ChannelSaveIO bean){
        ChannelDTO channelDTO =  BeanToolsUtil.copyOrReturnNull(bean, ChannelDTO.class);
        channelService.saveChannel(channelDTO);
        return ResponseResult.success();
    }

    /**
     * 修改栏目信息
     * @param bean
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "修改栏目信息")
    @RequestMapping(value = "/updateChannel",method = RequestMethod.POST)
    @RequiresPermissions("/channel/updateChannel")
    public ResponseResult<Object> updateChannel(@Validated @RequestBody ChannelUpdateIO bean){
        ChannelDTO channelDTO = BeanToolsUtil.copyOrReturnNull(bean, ChannelDTO.class);
        channelService.updateChannel(channelDTO);
        return ResponseResult.success();
    }

    /**
     * 删除栏目信息
     * @param req
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "删除栏目信息")
    @RequestMapping(value="/deleteChannelByID", method= RequestMethod.POST)
    @RequiresPermissions("/channel/deleteChannelByID")
    public ResponseResult<Object> deleteChannelByID(@Valid @RequestBody IdIO req) {
        channelService.deleteChannelByID(req.getId());
        return ResponseResult.success();
    }

}