package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
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
    @RequestMapping(value = "/getChannelListByPage",method = RequestMethod.GET)
    public ResponseResult<Object> getChannelListByPage(@Validated @RequestBody ChannelQueryIO channelQueryIO){
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
    @RequestMapping(value = "/getChannelByID",method = RequestMethod.GET)
    public ResponseResult<Object> getChannelByID(@RequestParam("id") Long id){
        ChannelDTO channelDTO = channelService.getChannelByID(id);
        ChannelVO channelVO = BeanToolsUtil.copyOrReturnNull(channelDTO,ChannelVO.class);
        return ResponseResult.success(channelVO);
    }

    /**
     * 新增栏目信息
     * @param bean
     * @return
     */
    @RequestMapping(value = "/saveChannel",method = RequestMethod.POST)
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
    @RequestMapping(value = "/updateChannel",method = RequestMethod.POST)
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
    @RequestMapping(value="/deleteChannelByID", method= RequestMethod.POST)
    public ResponseResult<Object> deleteChannelByID(@Valid @RequestBody IdIO req) {
        channelService.deleteChannelByID(req.getId());
        return ResponseResult.success();
    }

}