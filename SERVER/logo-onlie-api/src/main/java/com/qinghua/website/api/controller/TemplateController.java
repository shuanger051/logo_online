package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.controller.vo.TemplateVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.TemplateDTO;
import com.qinghua.website.server.service.TemplateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     * 分页查询模板信息
     * @param templateQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询模板信息")
    @RequestMapping(value = "/getTemplateListByPage",method = RequestMethod.GET)
    @RequiresPermissions("/template/getTemplateListByPage")
    public ResponseResult<Object> getTemplateListByPage(@Validated @RequestBody TemplateQueryIO templateQueryIO){
        TemplateDTO queryDTO = BeanToolsUtil.copyOrReturnNull(templateQueryIO,TemplateDTO.class);
        PageInfo<TemplateDTO> pageList =  templateService.getTemplateListByPage(queryDTO);
        List<TemplateVO> templateVOList =  BeanToolsUtil.copyAsList(pageList.getList(),TemplateVO.class);
        PageListVO<TemplateVO> resp = new PageListVO<>();
        resp.setList(templateVOList);
        resp.setTotal(pageList.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * 根据ID查询模板数据
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询模板数据")
    @RequestMapping(value = "/getTemplateByID", method = RequestMethod.GET)
    @RequiresPermissions("/template/getTemplateByID")
    public ResponseResult<Object> getTemplateByID(@RequestParam("id") Long id){
        TemplateDTO templateDTO = templateService.getTemplateById(id);
        TemplateVO templateVO = BeanToolsUtil.copyOrReturnNull(templateDTO,TemplateVO.class);
        return ResponseResult.success(templateVO);
    }

    /**
     * 新增模板信息
     * @param templateSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增模板信息")
    @RequestMapping(value = "/saveTemplate", method = RequestMethod.POST)
    @RequiresPermissions("/template/saveTemplate")
    public ResponseResult<Object> saveTemplate(@Validated @RequestBody TemplateSaveIO templateSaveIO){
        TemplateDTO saveDTO = BeanToolsUtil.copyOrReturnNull(templateSaveIO,TemplateDTO.class);
        templateService.saveTemplate(saveDTO);
        return ResponseResult.success();
    }

    /**
     * 修改模板信息
     * @param templateUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "修改模板信息")
    @RequestMapping(value = "/updateTemplate", method = RequestMethod.POST)
    @RequiresPermissions("/template/updateTemplate")
    public ResponseResult<Object> updateTemplate(@Validated @RequestBody TemplateUpdateIO templateUpdateIO){
        TemplateDTO update = BeanToolsUtil.copyOrReturnNull(templateUpdateIO,TemplateDTO.class);
        templateService.updateTemplateById(update);
        return ResponseResult.success();
    }

    /**
     * 根据ID删除模板信息
     * @param idIO
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "根据ID删除模板信息")
    @RequestMapping(value = "/deleteTemplateByID", method = RequestMethod.POST)
    @RequiresPermissions("/template/deleteTemplateByID")
    public ResponseResult<Object> deleteTemplateByID(@Valid @RequestBody IdIO idIO){
        templateService.deleteTemplateById(idIO.getId());
        return ResponseResult.success();
    }

    /**
     * 根据ID更改模板发布状态
     * @param templateStatusIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "根据ID更改模板发布状态")
    @RequestMapping(value = "/updateTemplateStatusById", method = RequestMethod.POST)
    @RequiresPermissions("/template/updateTemplateStatusById")
    public ResponseResult<Object> updateTemplateStatusById(@Validated @RequestBody TemplateStatusIO templateStatusIO){
        TemplateDTO update = BeanToolsUtil.copyOrReturnNull(templateStatusIO,TemplateDTO.class);
        templateService.updateTemplateStatusById(update);
        return ResponseResult.success();
    }

}
