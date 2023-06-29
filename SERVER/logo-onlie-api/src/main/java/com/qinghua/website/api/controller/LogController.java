package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.LogQueryIO;
import com.qinghua.website.api.controller.vo.LogInfoVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.LogInfoDTO;
import com.qinghua.website.server.service.LogInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户信息
 */
@Slf4j
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogInfoService logInfoService;

    /**
     * 分页查询日志列表
     * @param logQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询日志列表")
    @RequestMapping(value = "/getLogInfoListByPage",method = RequestMethod.GET)
    public ResponseResult<Object> getLogInfoListByPage(@Validated @RequestBody LogQueryIO logQueryIO){
        LogInfoDTO logInfoDTO =  BeanToolsUtil.copyOrReturnNull(logQueryIO, LogInfoDTO.class);
        PageInfo<LogInfoDTO> pageList = logInfoService.getLogInfoListByPage(logInfoDTO);
        List<LogInfoVO> logInfoVOList =  BeanToolsUtil.copyAsList(pageList.getList(), LogInfoVO.class);
        PageListVO<LogInfoVO> result = new PageListVO<>();
        result.setList(logInfoVOList);
        result.setTotal(pageList.getTotal());
        return ResponseResult.success(result);
    }

    /**
     * 根据ID查询日志信息
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询日志信息")
    @RequestMapping(value = "/getLogInfoById",method = RequestMethod.GET)
    public ResponseResult<Object> getLogInfoById(@RequestParam("id") Long id){
        LogInfoDTO logInfoDTO = logInfoService.getLogInfoById(id);
        LogInfoVO logInfoVO = BeanToolsUtil.copyOrReturnNull(logInfoDTO,LogInfoVO.class);
        return ResponseResult.success(logInfoVO);
    }

}
