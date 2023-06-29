package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.controller.io.IdIO;
import com.qinghua.website.api.controller.io.ShopsInfoQueryIO;
import com.qinghua.website.api.controller.io.ShopsInfoSaveIO;
import com.qinghua.website.api.controller.io.ShopsInfoUpdateIO;
import com.qinghua.website.api.controller.vo.ShopsInfoVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.ShopsInfoDTO;
import com.qinghua.website.server.service.ShopsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @RequestMapping(value = "/getShopsInfoById",method = RequestMethod.GET)
    public ResponseResult<Object> getShopsInfoById(@RequestParam("id") Long id){
        ShopsInfoDTO shopsInfoDTO = shopsInfoService.getShopsInfoById(id);
        ShopsInfoVO shopsInfoVO = BeanToolsUtil.copyOrReturnNull(shopsInfoDTO,ShopsInfoVO.class);
        return ResponseResult.success(shopsInfoVO);
    }

    /**
     * 新增店铺信息
     * @param bean
     * @return
     */
    @RequestMapping(value = "/saveShopsInfo",method = RequestMethod.POST)
    public ResponseResult<Object> saveShopsInfo(@Validated @RequestBody ShopsInfoSaveIO bean){
        ShopsInfoDTO shopsInfoDTO =  BeanToolsUtil.copyOrReturnNull(bean, ShopsInfoDTO.class);
        shopsInfoService.saveShopsInfo(shopsInfoDTO);
        return ResponseResult.success();
    }

    /**
     * 修改店铺信息
     * @param bean
     * @return
     */
    @RequestMapping(value = "/updateShopsInfoById",method = RequestMethod.POST)
    public ResponseResult<Object> updateShopsInfoById(@Validated @RequestBody ShopsInfoUpdateIO bean){
        ShopsInfoDTO shopsInfoDTO = BeanToolsUtil.copyOrReturnNull(bean, ShopsInfoDTO.class);
        shopsInfoService.updateShopsInfoById(shopsInfoDTO);
        return ResponseResult.success();
    }

    /**
     * 删除店铺信息
     * @param req
     * @return
     */
    @RequestMapping(value="/deleteShopsInfoById", method= RequestMethod.POST)
    public ResponseResult<Object> deleteShopsInfoById(@Valid @RequestBody IdIO req) {
        shopsInfoService.deleteShopsInfoById(req.getId());
        return ResponseResult.success();
    }

}
