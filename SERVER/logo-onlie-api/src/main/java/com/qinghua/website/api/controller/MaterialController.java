package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.MaterialVO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.MaterialDTO;
import com.qinghua.website.server.service.MaterialService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    /**
     * 分页查询素材信息
     * @param materialQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询素材信息")
    @RequestMapping("/getMaterialListByPage")
    @RequiresPermissions("/material/getMaterialListByPage")
    public ResponseResult<Object> getMaterialListByPage(@Validated MaterialQueryIO materialQueryIO){
        MaterialDTO queryDTO = BeanToolsUtil.copyOrReturnNull(materialQueryIO,MaterialDTO.class);
        PageInfo<MaterialDTO> pageList =  materialService.getMaterialListByPage(queryDTO);
        List<MaterialVO> materialVOList =  BeanToolsUtil.copyAsList(pageList.getList(),MaterialVO.class);

        materialVOList.forEach(item->item.setUrlPath("/savePath/material/" + item.getFilePath() + "/" + item.getFileName()));

        PageListVO<MaterialVO> resp = new PageListVO<>();
        resp.setList(materialVOList);
        resp.setTotal(pageList.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * 分页查询素材信息(OSS)
     * @param materialQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询素材信息(OSS)")
    @RequestMapping("/getMaterialListByPageOSS")
    @RequiresPermissions("/material/getMaterialListByPageOSS")
    public ResponseResult<Object> getMaterialListByPageOSS(@Validated MaterialQueryIO materialQueryIO){
        MaterialDTO queryDTO = BeanToolsUtil.copyOrReturnNull(materialQueryIO,MaterialDTO.class);
        PageInfo<MaterialDTO> pageList =  materialService.getMaterialListByPage(queryDTO);
        List<MaterialVO> materialVOList =  BeanToolsUtil.copyAsList(pageList.getList(),MaterialVO.class);

        PageListVO<MaterialVO> resp = new PageListVO<>();
        resp.setList(materialVOList);
        resp.setTotal(pageList.getTotal());
        return ResponseResult.success(resp);
    }


    /**
     * 根据ID查询素材数据
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询素材数据")
    @RequestMapping(value = "/getMaterialByID", method = RequestMethod.GET)
    @RequiresPermissions("/material/getMaterialByID")
    public ResponseResult<Object> getMaterialByID(@RequestParam("id") Long id){
        MaterialDTO material = materialService.getMaterialById(id);
        MaterialVO materialVO = BeanToolsUtil.copyOrReturnNull(material,MaterialVO.class);
        if(null != materialVO){
            materialVO.setUrlPath("/savePath/material/" + materialVO.getFilePath() + "/" + materialVO.getFileName());
        }
        return ResponseResult.success(null == materialVO ? "无数据" : materialVO);
    }

    /**
     * 根据ID查询素材数据(OSS)
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询素材数据(OSS)")
    @RequestMapping(value = "/getMaterialByIDOSS", method = RequestMethod.GET)
    @RequiresPermissions("/material/getMaterialByIDOSS")
    public ResponseResult<Object> getMaterialByIDOSS(@RequestParam("id") Long id){
        MaterialDTO material = materialService.getMaterialById(id);
        MaterialVO materialVO = BeanToolsUtil.copyOrReturnNull(material,MaterialVO.class);
        return ResponseResult.success(null == materialVO ? "无数据" : materialVO);
    }

    /**
     * 新增素材信息
     * @param materialSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增素材信息")
    @RequestMapping(value = "/saveMaterial", method = RequestMethod.POST)
    @RequiresPermissions("/material/saveMaterial")
    public ResponseResult<Object> saveMaterial(@Validated @RequestBody MaterialSaveIO materialSaveIO){
        MaterialDTO saveDTO = BeanToolsUtil.copyOrReturnNull(materialSaveIO,MaterialDTO.class);
        materialService.saveMaterial(saveDTO);
        return ResponseResult.success();
    }

    /**
     * 根据ID修改素材信息
     * @param materialUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "根据ID修改素材信息")
    @RequestMapping(value = "/updateMaterialById", method = RequestMethod.POST)
    @RequiresPermissions("/material/updateMaterialById")
    public ResponseResult<Object> updateMaterialById(@Validated @RequestBody MaterialUpdateIO materialUpdateIO){
        MaterialDTO update = BeanToolsUtil.copyOrReturnNull(materialUpdateIO,MaterialDTO.class);
        materialService.updateMaterialById(update);
        return ResponseResult.success();
    }

    /**
     * 根据ID删除素材信息
     * @param idIO
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "根据ID删除素材信息")
    @RequestMapping(value = "/deleteMaterialByID", method = RequestMethod.POST)
    @RequiresPermissions("/template/deleteMaterialByID")
    public ResponseResult<Object> deleteMaterialByID(@Valid @RequestBody IdIO idIO){
        materialService.deleteMaterialById(idIO.getId());
        return ResponseResult.success();
    }

    /**
     * 根据ID删除素材信息(oss)
     * @param idIO
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "根据ID删除素材信息(oss)")
    @RequestMapping(value = "/deleteMaterialByIDOSS", method = RequestMethod.POST)
    @RequiresPermissions("/template/deleteMaterialByIDOSS")
    public ResponseResult<Object> deleteMaterialByIDOSS(@Valid @RequestBody IdIO idIO){
        materialService.deleteMaterialByIdOSS(idIO.getId());
        return ResponseResult.success();
    }

}
