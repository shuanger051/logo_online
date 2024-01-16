package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.LogoInfoVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.LogoInfoDTO;
import com.qinghua.website.server.service.LogoInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/logo")
public class LogoController {

    @Autowired
    private LogoInfoService logoInfoService;

    /**
     * 分页查询店招信息
     * @param logoQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询店招信息")
    @RequestMapping("/getLogoListByPage")
    @RequiresPermissions("/logo/getLogoListByPage")
    public ResponseResult<Object> getLogoListByPage(@Validated LogoQueryIO logoQueryIO){
        LogoInfoDTO queryDTO = BeanToolsUtil.copyOrReturnNull(logoQueryIO,LogoInfoDTO.class);
        PageInfo<LogoInfoDTO> pageList =  logoInfoService.getLogoInfoListByPage(queryDTO);
        List<LogoInfoVO> logoInfoVOList =  BeanToolsUtil.copyAsList(pageList.getList(),LogoInfoVO.class);
        PageListVO<LogoInfoVO> resp = new PageListVO<>();
        resp.setList(logoInfoVOList);
        resp.setTotal(pageList.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * 分页查询店招信息(OSS)
     * @param logoQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询店招信息(OSS)")
    @RequestMapping("/getLogoListByPageOSS")
    @RequiresPermissions("/logo/getLogoListByPageOSS")
    public ResponseResult<Object> getLogoListByPageOSS(@Validated LogoQueryIO logoQueryIO){
        LogoInfoDTO queryDTO = BeanToolsUtil.copyOrReturnNull(logoQueryIO,LogoInfoDTO.class);
        PageInfo<LogoInfoDTO> pageList =  logoInfoService.getLogoInfoListByPage(queryDTO);
        List<LogoInfoVO> logoInfoVOList =  BeanToolsUtil.copyAsList(pageList.getList(),LogoInfoVO.class);
        PageListVO<LogoInfoVO> resp = new PageListVO<>();
        resp.setList(logoInfoVOList);
        resp.setTotal(pageList.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * 根据ID查询店招数据
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询店招数据")
    @RequestMapping(value = "/getLogoInfoByID", method = RequestMethod.GET)
    @RequiresPermissions("/logo/getLogoInfoByID")
    public ResponseResult<Object> getLogoInfoByID(@RequestParam("id") Long id){
        LogoInfoDTO logoInfoDTO = logoInfoService.getLogoInfoById(id);
        LogoInfoVO logoInfoVO = BeanToolsUtil.copyOrReturnNull(logoInfoDTO,LogoInfoVO.class);
        return ResponseResult.success(logoInfoVO);
    }

    /**
     * 新增店招信息
     * @param logoInfoSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增店招信息")
    @RequestMapping(value = "/saveLogoInfo", method = RequestMethod.POST)
    @RequiresPermissions("/logo/saveLogoInfo")
    public ResponseResult<Object> saveLogoInfo(@Validated @RequestBody LogoInfoSaveIO logoInfoSaveIO){
        LogoInfoDTO saveDTO = BeanToolsUtil.copyOrReturnNull(logoInfoSaveIO,LogoInfoDTO.class);
        logoInfoService.saveLogoInfo(saveDTO);
        return ResponseResult.success();
    }

    /**
     * 根据ID修改店招信息
     * @param logoInfoUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "根据ID修改店招信息")
    @RequestMapping(value = "/updateLogoInfoById", method = RequestMethod.POST)
    @RequiresPermissions("/logo/updateLogoInfoById")
    public ResponseResult<Object> updateLogoInfoById(@Validated @RequestBody LogoInfoUpdateIO logoInfoUpdateIO){
        LogoInfoDTO update = BeanToolsUtil.copyOrReturnNull(logoInfoUpdateIO,LogoInfoDTO.class);
        logoInfoService.updateLogoInfoById(update);
        return ResponseResult.success();
    }

    /**
     * 根据ID删除店招信息
     * @param idIO
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "根据ID删除店招信息")
    @RequestMapping(value = "/deleteLogoInfoByID", method = RequestMethod.POST)
    @RequiresPermissions("/logo/deleteLogoInfoByID")
    public ResponseResult<Object> deleteLogoInfoByID(@Valid @RequestBody IdIO idIO){
        logoInfoService.deleteLogoInfoById(idIO.getId());
        return ResponseResult.success();
    }

}
