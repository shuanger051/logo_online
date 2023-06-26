package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.controller.io.IdIO;
import com.qinghua.website.api.controller.io.SysPermissionQueryIO;
import com.qinghua.website.api.controller.io.SysPermissionSaveIO;
import com.qinghua.website.api.controller.io.SysPermissionUpdateIO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.controller.vo.SysPermissionVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.domain.SysPermissionDTO;
import com.qinghua.website.server.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName SysPermissionController
 * @Description 系统权限管理Controller
 **/
@Slf4j
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController {
    
    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 根据ID查询权限信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/getSysPermissionById", method = RequestMethod.GET)
    public ResponseResult<Object> getSysPermissionById(@RequestParam("id") Long id) {
        SysPermissionDTO sysPermissionDTO = sysPermissionService.getSysPermissionById(id);
        SysPermissionVO sysPermissionVO = BeanToolsUtil.copyOrReturnNull(sysPermissionDTO, SysPermissionVO.class);
        return ResponseResult.success(sysPermissionVO);
    }

    /**
     * 分页查询权限信息
     * @param sysPermissionQueryIO
     * @return
     */
    @RequestMapping(value = "/getSysPermissionListByPage", method = RequestMethod.GET)
    public ResponseResult<Object> getSysPermissionListByPage(@Validated @RequestBody SysPermissionQueryIO sysPermissionQueryIO) {
        SysPermissionDTO sysPermissionDTO =  BeanToolsUtil.copyOrReturnNull(sysPermissionQueryIO, SysPermissionDTO.class);
        PageInfo<SysPermissionDTO> pagelist = sysPermissionService.getSysPermissionListByPage(sysPermissionDTO);
        List<SysPermissionVO> sysPermissionVOList = BeanToolsUtil.copyAsList(pagelist.getList(), SysPermissionVO.class);
        PageListVO<SysPermissionVO> resp = new PageListVO<>();
        resp.setList(sysPermissionVOList);
        resp.setTotal(pagelist.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * 查询所有权限信息
     * @return
     */
    @RequestMapping(value = "/getSysPermissionList", method = RequestMethod.GET)
    public ResponseResult<Object> getSysPermissionList() {
        SysPermissionDTO sysPermissionDTO = new SysPermissionDTO();
        List<SysPermissionDTO> list = sysPermissionService.getSysPermissionList(sysPermissionDTO);
        List<SysPermissionVO> sysPermissionVOList = BeanToolsUtil.copyAsList(list, SysPermissionVO.class);
        return ResponseResult.success(sysPermissionVOList);
    }

    /**
     * 根据角色ID查询权限信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/getPermissionByRoleId", method = RequestMethod.GET)
    public ResponseResult<Object> getPermissionByRoleId(@RequestParam("id") Long id) {
        List<SysPermissionDTO> permissionList = sysPermissionService.getPermissionByRoleId(id);
        List<SysPermissionVO> permissionVOList = BeanToolsUtil.copyAsList(permissionList, SysPermissionVO.class);
        return ResponseResult.success(permissionVOList);
    }

    /**
     * 新增权限信息
     * @param sysPermissionSaveIO
     * @return
     */
    @RequestMapping(value = "/saveSysPermission", method = RequestMethod.POST)
    public ResponseResult<Object> saveSysPermission(@Validated @RequestBody SysPermissionSaveIO sysPermissionSaveIO) {
        SysPermissionDTO sysPermissionDTO =  BeanToolsUtil.copyOrReturnNull(sysPermissionSaveIO, SysPermissionDTO.class);
        sysPermissionService.saveSysPermission(sysPermissionDTO);
        return ResponseResult.success();
    }


    /**
     * 根据ID修改权限信息
     * @param sysPermissionUpdateIO
     * @return
     */
    @RequestMapping(value = "/updateSysPermissionById", method = RequestMethod.POST)
    public ResponseResult<Object> updateSysPermissionById(@Validated @RequestBody SysPermissionUpdateIO sysPermissionUpdateIO) {
        SysPermissionDTO sysPermissionDTO =  BeanToolsUtil.copyOrReturnNull(sysPermissionUpdateIO, SysPermissionDTO.class);
        sysPermissionService.updateSysPermissionById(sysPermissionDTO);
        return ResponseResult.success();
    }

    /**
     * 根据ID删除权限信息
     * @param idIO
     * @return
     */
    @RequestMapping(value = "/deleteSysPermissionById", method = RequestMethod.POST)
    public ResponseResult<Object> deleteSysPermissionById(@Validated @RequestBody IdIO idIO) {
        sysPermissionService.deleteSysPermissionById(idIO.getId());
        return ResponseResult.success();
    }

}
