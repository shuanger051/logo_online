package com.qinghua.website.server.service;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.domain.SysUserRoleDTO;

import java.util.List;

/**
 * @ClassName SysUserRoleService
 * @Description 系统用户角色关系管理Service层
 **/
public interface SysUserRoleService {

    /**
     * 根据ID查询用户角色信息
     * @param id
     * @return
     */
    SysUserRoleDTO getSysUserRoleById(Long id);

    /***
     * 查询用户角色息集合
     * @param sysUserRoleDTO
     * @return
     */
    List<SysUserRoleDTO> getSysUserRoleList(SysUserRoleDTO sysUserRoleDTO);


    /***
     * 分页查询用户角色集合
     * @param sysUserRoleDTO
     * @return
     */
    PageInfo<SysUserRoleDTO> getSysUserRoleListByPage(SysUserRoleDTO sysUserRoleDTO);


    /**
     * 新增用户角色信息
     * @param sysUserRoleDTO
     */
    void saveSysUserRole(SysUserRoleDTO sysUserRoleDTO);

    /**
     * 修改用户角色信息
     * @param sysUserRoleDTO
     */
    void updateSysUserRoleById(SysUserRoleDTO sysUserRoleDTO);

    /**
     * 根据ID删除用户角色信息
     * @param id
     */
    void deleteSysUserRoleById(Long id);

    /**
     * 批量新增用户角色信息
     * @param sysUserRoleDTOList
     * @return
     */
    void saveSysUserRoleForBatch(List<SysUserRoleDTO> sysUserRoleDTOList);

}
