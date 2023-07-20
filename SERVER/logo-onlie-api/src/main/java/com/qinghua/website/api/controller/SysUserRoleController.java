package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.controller.io.IdIO;
import com.qinghua.website.api.controller.io.SysUserRoleQueryIO;
import com.qinghua.website.api.controller.io.SysUserRoleSaveIO;
import com.qinghua.website.api.controller.io.SysUserRoleUpdateIO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.controller.vo.SysUserRoleVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.api.validation.ValidList;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.SysUserRoleDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

/**
 * @ClassName SysUserRoleController
 * @Description 系统权限管理Controller
 **/
@Slf4j
@RestController
@RequestMapping("/sys/user-role")
public class SysUserRoleController {
    
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 根据ID查询用户角色信息
     * @param id
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "根据ID查询用户角色信息")
    @RequestMapping(value = "/getSysUserRoleById", method = RequestMethod.GET)
    @RequiresPermissions("/sys/user-role/getSysUserRoleById")
    public ResponseResult<Object> getSysUserRoleById(@RequestParam("id") Long id) {
        SysUserRoleDTO sysUserRoleDTO = sysUserRoleService.getSysUserRoleById(id);
        SysUserRoleVO sysUserRoleVO =  BeanToolsUtil.copyOrReturnNull(sysUserRoleDTO,SysUserRoleVO.class);
        return ResponseResult.success(sysUserRoleVO);
    }

    /**
     * 分页查询用户角色信息
     * @param sysUserRoleQueryReq
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询用户角色信息")
    @RequestMapping(value = "/getSysUserRoleListByPage", method = RequestMethod.GET)
    @RequiresPermissions("/sys/user-role/getSysUserRoleListByPage")
    public ResponseResult<Object> getSysUserRoleListByPage(@Validated SysUserRoleQueryIO sysUserRoleQueryReq) {
        SysUserRoleDTO sysUserRoleDTO = BeanToolsUtil.copyOrReturnNull(sysUserRoleQueryReq, SysUserRoleDTO.class);
        PageInfo<SysUserRoleDTO> pagelist = sysUserRoleService.getSysUserRoleListByPage(sysUserRoleDTO);
        List<SysUserRoleVO> sysUserRoleVOList =  BeanToolsUtil.copyAsList(pagelist.getList(), SysUserRoleVO.class);
        PageListVO<SysUserRoleVO> resp = new PageListVO<>();
        resp.setList(sysUserRoleVOList);
        resp.setTotal(pagelist.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * 新增用户角色信息
     * @param sysUserRoleSaveReq
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增用户角色信息")
    @RequestMapping(value = "/saveSysUserRole", method = RequestMethod.POST)
    @RequiresPermissions("/sys/user-role/saveSysUserRole")
    public ResponseResult<Object> saveSysUserRole(@Validated @RequestBody SysUserRoleSaveIO sysUserRoleSaveReq) {
        SysUserRoleDTO sysUserRoleDTO =  BeanToolsUtil.copyOrReturnNull(sysUserRoleSaveReq, SysUserRoleDTO.class);
        sysUserRoleService.saveSysUserRole(sysUserRoleDTO);
        return ResponseResult.success();
    }


    /**
     * 根据ID修改用户角色信息
     * @param sysUserRoleUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "根据ID修改用户角色信息")
    @RequestMapping(value = "/updateSysUserRoleById", method = RequestMethod.POST)
    @RequiresPermissions("/sys/user-role/updateSysUserRoleById")
    public ResponseResult<Object> updateSysUserRoleById(@Validated @RequestBody SysUserRoleUpdateIO sysUserRoleUpdateIO) {
        SysUserRoleDTO sysUserRoleDTO = BeanToolsUtil.copyOrReturnNull(sysUserRoleUpdateIO, SysUserRoleDTO.class);
        sysUserRoleService.updateSysUserRoleById(sysUserRoleDTO);
        return ResponseResult.success();
    }

    /**
     * 根据ID删除用户角色信息
     * @param idIO
     * @return
     */
    @LogAnnotation(logType = "delete",logDesc = "根据ID删除用户角色信息")
    @RequestMapping(value = "/deleteSysUserRoleById", method = RequestMethod.POST)
    @RequiresPermissions("/sys/user-role/deleteSysUserRoleById")
    public ResponseResult<Object> deleteSysUserRoleById(@Validated @RequestBody IdIO idIO) {
        sysUserRoleService.deleteSysUserRoleById(idIO.getId());
        return ResponseResult.success();
    }

    /**
     * 批量新增或修改用户角色信息
     * @param sysUserRoleSaveReqList
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "批量新增或修改用户角色信息")
    @RequestMapping(value = "/saveOrUpdateSysUserRoleForBatch",method = RequestMethod.POST)
    @RequiresPermissions("/sys/user-role/saveOrUpdateSysUserRoleForBatch")
    public ResponseResult<Object> saveOrUpdateSysUserRoleForBatch(@Validated @RequestBody ValidList<SysUserRoleSaveIO> sysUserRoleSaveReqList){

        //判断提交的List集合是否存在重复的数据
        HashSet<SysUserRoleSaveIO> hashSet = new HashSet<>(sysUserRoleSaveReqList);
        if(sysUserRoleSaveReqList.size() != hashSet.size()){
            throw new BizException(SysConstant.ERROR_WRONG_LIST_PARAMS_10014);
        }
        List<SysUserRoleDTO> sysUserRoleDTOList = BeanToolsUtil.copyAsList(sysUserRoleSaveReqList,SysUserRoleDTO.class);
        sysUserRoleService.saveSysUserRoleForBatch(sysUserRoleDTOList);
        return ResponseResult.success();
    }
}
