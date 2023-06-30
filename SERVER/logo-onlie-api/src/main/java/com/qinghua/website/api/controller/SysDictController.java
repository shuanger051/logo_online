package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.controller.vo.SysDictVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.SysDictDTO;
import com.qinghua.website.server.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName SysDictController
 * @Description 数据字典controller
 **/
@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 数据字典分页查询
     * @param sysDictQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "数据字典分页查询")
    @RequestMapping(value = "/getDictListByPage", method = RequestMethod.GET)
    public ResponseResult<Object> getDictListByPage(@Validated @RequestBody SysDictQueryIO sysDictQueryIO) {
        SysDictDTO sysDictDTO =  BeanToolsUtil.copyOrReturnNull(sysDictQueryIO, SysDictDTO.class);
        PageInfo<SysDictDTO> pageList = sysDictService.getDictListByPage(sysDictDTO);
        List<SysDictVO> sysDictList =  BeanToolsUtil.copyAsList(pageList.getList(), SysDictVO.class);
        PageListVO<SysDictVO> result = new PageListVO<>();
        result.setList(sysDictList);
        result.setTotal(pageList.getTotal());
        return ResponseResult.success(result);
    }

    /**
     * 数据字典类型数据添加
     * @param sysDictSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "数据字典类型数据添加")
    @RequestMapping(value = "/saveDict", method = RequestMethod.POST)
    public ResponseResult<Object> saveDict(@Valid @RequestBody SysDictSaveIO sysDictSaveIO) {
        SysDictDTO sysDictDTO =  BeanToolsUtil.copyOrReturnNull(sysDictSaveIO, SysDictDTO.class);
        sysDictService.saveDict(sysDictDTO);
        return ResponseResult.success();
    }

    /**
     * 数据字典类型数据修改
     * @param sysDictUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "数据字典类型数据修改")
    @RequestMapping(value = "/updateDictById", method = RequestMethod.POST)
    public ResponseResult<Object> updateDictById(@Valid @RequestBody SysDictUpdateIO sysDictUpdateIO) {
        SysDictDTO sysDictDTO = BeanToolsUtil.copyOrReturnNull(sysDictUpdateIO, SysDictDTO.class);
        sysDictService.updateDictById(sysDictDTO);
        return ResponseResult.success();
    }

    /**
     * 数据字典类型查看
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "数据字典类型查看")
    @RequestMapping(value = "/getDictById", method = RequestMethod.GET)
    public ResponseResult<Object> getDictById(@RequestParam("id") Long id) {
        SysDictDTO dict = sysDictService.getDictById(id);
        SysDictVO sysDictVO =  BeanToolsUtil.copyOrReturnNull(dict, SysDictVO.class);
        return ResponseResult.success(sysDictVO);
    }

    /**
     * 数据字典类型数据删除
     * @param req
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "数据字典类型数据删除")
    @RequestMapping(value = "/deleteDictById", method = RequestMethod.POST)
    public ResponseResult<Object> deleteDictById(@Valid @RequestBody IdIO req) {
        sysDictService.deleteDictById(req.getId());
        return ResponseResult.success();
    }
}
