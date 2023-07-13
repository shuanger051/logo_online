package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.IdIO;
import com.qinghua.website.api.controller.io.SysRolePermissionQueryIO;
import com.qinghua.website.api.controller.io.SysRolePermissionSaveIO;
import com.qinghua.website.api.controller.io.SysRolePermissionUpdateIO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.controller.vo.SysRolePermissionVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.api.validation.ValidList;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.SysRolePermissionDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysRolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @ClassName SysRolePermissionController
 * @Description 系统角色权限管理Controller
 **/
@Slf4j
@RestController
@RequestMapping("/sys/role-permission")
public class SysRolePermissionController {

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    /**
     * 根据ID查询角色权限信息
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询角色权限信息")
    @RequestMapping(value = "/getSysRolePermissionById", method = RequestMethod.GET)
    @RequiresPermissions("/sys/role-permission/getSysRolePermissionById")
    public ResponseResult<Object> getSysRolePermissionById(@RequestParam("id") Long id) {
        SysRolePermissionDTO sysRolePermissionDTO = sysRolePermissionService.getSysRolePermissionById(id);
        SysRolePermissionVO sysRolePermissionVO = BeanToolsUtil.copyOrReturnNull(sysRolePermissionDTO, SysRolePermissionVO.class);
        return ResponseResult.success(sysRolePermissionVO);
    }

    /**
     * 根据角色ID查询角色权限信息
     * @param roleId
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据角色ID查询角色权限信息")
    @RequestMapping(value = "/getSysRolePermissionListByRoleId", method = RequestMethod.GET)
    @RequiresPermissions("/sys/role-permission/getSysRolePermissionListByRoleId")
    public ResponseResult<Object> getSysRolePermissionListByRoleId(@RequestParam("roleId") Long roleId) {
        List<SysRolePermissionDTO> sysRolePermissionList = sysRolePermissionService.getSysRolePermissionListByRoleId(roleId);
        List<SysRolePermissionVO> sysRolePermissionVOList = BeanToolsUtil.copyAsList(sysRolePermissionList, SysRolePermissionVO.class);
        return ResponseResult.success(sysRolePermissionVOList);
    }

    /**
     * 批量新增或更新角色权限信息
     * @param sysRolePermissionSaveReqList
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "批量新增或更新角色权限信息")
    @RequestMapping(value = "/saveOrUpdateSysRolePermissionForBatch")
    @RequiresPermissions("/sys/role-permission/saveOrUpdateSysRolePermissionForBatch")
    public ResponseResult<Object> saveOrUpdateSysRolePermissionForBatch(@Validated @RequestBody ValidList<SysRolePermissionSaveIO> sysRolePermissionSaveReqList) {

        //判断提交的List集合是否存在重复的数据
        HashSet<SysRolePermissionSaveIO> hashSet = new HashSet<>(sysRolePermissionSaveReqList);
        if (sysRolePermissionSaveReqList.size() != hashSet.size()) {
            throw new BizException(SysConstant.ERROR_WRONG_LIST_PARAMS_10014);
        }
        List<SysRolePermissionDTO> sysRolePermissionDTOList = BeanToolsUtil.copyAsList(sysRolePermissionSaveReqList, SysRolePermissionDTO.class);
        sysRolePermissionService.saveSysRolePermissionForBatch(sysRolePermissionDTOList);
        return ResponseResult.success();

    }

    /**
     * 分页查询角色权限信息
     * @param sysRolePermissionQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询角色权限信息")
    @RequestMapping(value = "/getSysRolePermissionListByPage", method = RequestMethod.GET)
    @RequiresPermissions("/sys/role-permission/getSysRolePermissionListByPage")
    public ResponseResult<Object> getSysRolePermissionListByPage(@Validated @RequestBody SysRolePermissionQueryIO sysRolePermissionQueryIO) {
        SysRolePermissionDTO sysRolePermissionDTO =  BeanToolsUtil.copyOrReturnNull(sysRolePermissionQueryIO, SysRolePermissionDTO.class);
        PageInfo<SysRolePermissionDTO> pagelist = sysRolePermissionService.getSysRolePermissionListByPage(sysRolePermissionDTO);

        List<SysRolePermissionVO> sysRolePermissionVOList = BeanToolsUtil.copyAsList(pagelist.getList(), SysRolePermissionVO.class);
        PageListVO<SysRolePermissionVO> resp = new PageListVO<>();
        resp.setList(sysRolePermissionVOList);
        resp.setTotal(pagelist.getTotal());

        return ResponseResult.success(resp);
    }

    /**
     * 新增角色权限信息
     * @param sysRolePermissionSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增角色权限信息")
    @RequestMapping(value = "/saveSysRolePermission", method = RequestMethod.POST)
    @RequiresPermissions("/sys/role-permission/saveSysRolePermission")
    public ResponseResult<Object> saveSysRolePermission(@Validated @RequestBody SysRolePermissionSaveIO sysRolePermissionSaveIO) {
        SysRolePermissionDTO sysRolePermissionDTO =  BeanToolsUtil.copyOrReturnNull(sysRolePermissionSaveIO, SysRolePermissionDTO.class);
        sysRolePermissionService.saveSysRolePermission(sysRolePermissionDTO);
        return ResponseResult.success();
    }

    /**
     * 根据ID修改角色权限信息
     * @param sysRolePermissionUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "根据ID修改角色权限信息")
    @RequestMapping(value = "/updateSysRolePermissionById", method = RequestMethod.POST)
    @RequiresPermissions("/sys/role-permission/updateSysRolePermissionById")
    public ResponseResult<Object> updateSysRolePermissionById(@Validated @RequestBody SysRolePermissionUpdateIO sysRolePermissionUpdateIO) {
        SysRolePermissionDTO sysRolePermissionDTO = BeanToolsUtil.copyOrReturnNull(sysRolePermissionUpdateIO, SysRolePermissionDTO.class);
        sysRolePermissionService.updateSysRolePermissionById(sysRolePermissionDTO);
        return ResponseResult.success();
    }

    /**
     * 根据ID删除角色权限信息
     * @param idIO
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "根据ID删除角色权限信息")
    @RequestMapping(value = "/deleteSysRolePermissionById", method = RequestMethod.POST)
    @RequiresPermissions("/sys/role-permission/deleteSysRolePermissionById")
    public ResponseResult<Object> deleteSysRolePermissionById(@Validated @RequestBody IdIO idIO) {
        sysRolePermissionService.deleteSysRolePermissionById(idIO.getId());
        return ResponseResult.success();
    }


}
