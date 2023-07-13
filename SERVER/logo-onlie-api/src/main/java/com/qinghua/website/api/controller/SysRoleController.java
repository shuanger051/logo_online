package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.controller.vo.SysRoleVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.SysRoleDTO;
import com.qinghua.website.server.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysRoleController
 * @Description 系统角色管理Controller
 **/
@Slf4j
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {
    
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 根据ID查询角色信息
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询角色信息")
    @RequestMapping(value = "/getSysRoleById", method = RequestMethod.GET)
    @RequiresPermissions("/sys/role/getSysRoleById")
    public ResponseResult<Object> getSysRoleById(@RequestParam("id") Long id) {
        SysRoleDTO sysRoleDTO = sysRoleService.getSysRoleById(id);
        SysRoleVO sysRoleVO =  BeanToolsUtil.copyOrReturnNull(sysRoleDTO,SysRoleVO.class);
        return ResponseResult.success(sysRoleVO);
    }

    /**
     * 分页查询角色信息
     * @param sysRoleQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询角色信息")
    @RequestMapping(value = "/getSysRoleListByPage", method = RequestMethod.GET)
    @RequiresPermissions("/sys/role/getSysRoleListByPage")
    public ResponseResult<Object> getSysRoleListByPage(@Validated @RequestBody SysRoleQueryIO sysRoleQueryIO) {
        SysRoleDTO sysRoleDTO =  BeanToolsUtil.copyOrReturnNull(sysRoleQueryIO, SysRoleDTO.class);
        PageInfo<SysRoleDTO> pageList = sysRoleService.getSysRoleListByPage(sysRoleDTO);
        List<SysRoleVO> sysRoleVOList =  BeanToolsUtil.copyAsList(pageList.getList(),SysRoleVO.class);
        PageListVO<SysRoleVO> resp = new PageListVO<>();
        resp.setList(sysRoleVOList);
        resp.setTotal(pageList.getTotal());

        return ResponseResult.success(resp);
    }

    /**
     * 新增角色信息
     * @param sysRoleSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增角色信息")
    @RequestMapping(value = "/saveSysRole", method = RequestMethod.POST)
    @RequiresPermissions("/sys/role/saveSysRole")
    public ResponseResult<Object> saveSysRole(@Validated @RequestBody SysRoleSaveIO sysRoleSaveIO) {
        SysRoleDTO sysRoleDTO = BeanToolsUtil.copyOrReturnNull(sysRoleSaveIO, SysRoleDTO.class);
        sysRoleService.saveSysRole(sysRoleDTO);
        return ResponseResult.success();
    }


    /**
     * 根据ID修改角色信息
     * @param sysRoleUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "根据ID修改角色信息")
    @RequestMapping(value = "/updateSysRoleById", method = RequestMethod.POST)
    @RequiresPermissions("/sys/role/updateSysRoleById")
    public ResponseResult<Object> updateSysRoleById(@Validated @RequestBody SysRoleUpdateIO sysRoleUpdateIO) {
        SysRoleDTO sysRoleDTO =  BeanToolsUtil.copyOrReturnNull(sysRoleUpdateIO, SysRoleDTO.class);
        sysRoleService.updateSysRoleById(sysRoleDTO);
        return ResponseResult.success();
    }

    /**
     * 根据ID删除角色信息
     * @param idIO
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "根据ID删除角色信息")
    @RequestMapping(value = "/deleteSysRoleById", method = RequestMethod.POST)
    @RequiresPermissions("/sys/role/deleteSysRoleById")
    public ResponseResult<Object> deleteSysRoleById(@Validated @RequestBody IdIO idIO) {
        SysRoleDTO sysRoleDTO = new SysRoleDTO();
        sysRoleDTO.setId(idIO.getId());
        sysRoleService.deleteSysRoleById(sysRoleDTO);
        return ResponseResult.success();
    }

    /**
     * 根据ID修改角色状态
     * @param sysRoleUpdateStatusIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "根据ID修改角色状态")
    @RequestMapping(value = "/updateSysRoleStatusById",method = RequestMethod.POST)
    @RequiresPermissions("/sys/role/updateSysRoleStatusById")
    public ResponseResult<Object> updateSysRoleStatusById(@Validated @RequestBody SysRoleUpdateStatusIO sysRoleUpdateStatusIO){
        SysRoleDTO sysRoleDTO =  BeanToolsUtil.copyOrReturnNull(sysRoleUpdateStatusIO,SysRoleDTO.class);
        sysRoleService.updateSysRoleStatusById(sysRoleDTO);
        return ResponseResult.success();
    }

}
