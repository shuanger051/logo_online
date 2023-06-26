package com.qinghua.website.server.service;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.domain.SysRolePermissionDTO;

import java.util.List;

/**
 * @ClassName SysRolePermissionService
 * @Description 系统角色权限关系管理Service层
 **/
public interface SysRolePermissionService {

    /**
     * 根据用户ID查询角色权限关系信息
     * @param id
     * @return
     */
    SysRolePermissionDTO getSysRolePermissionById(Long id);

    /***
     * 查询角色权限关系信息集合
     * @param sysRolePermissionDTO
     * @return
     */
    List<SysRolePermissionDTO> getSysRolePermissionList(SysRolePermissionDTO sysRolePermissionDTO);

    /**
     * 分页查询角色权限信息集合
     * @param sysRolePermissionDTO
     * @return
     */
    PageInfo<SysRolePermissionDTO> getSysRolePermissionListByPage(SysRolePermissionDTO sysRolePermissionDTO);

    /**
     * 保存角色权限关系信息
     * @param sysRolePermissionDTO
     */
    void saveSysRolePermission(SysRolePermissionDTO sysRolePermissionDTO);

    /**
     * 更新角色权限关系信息
     * @param sysRolePermissionDTO
     */
    void updateSysRolePermissionById(SysRolePermissionDTO sysRolePermissionDTO);

    /**
     * 根据ID删除角色权限关系
     * @param id
     */
    void deleteSysRolePermissionById(Long id);

    /**
     * 批量新增角色权限信息
     * @param sysRolePermissionDTOList
     */
    void saveSysRolePermissionForBatch(List<SysRolePermissionDTO> sysRolePermissionDTOList);

    /**
     * 根据角色ID查询角色权限
     */
    List<SysRolePermissionDTO> getSysRolePermissionListByRoleId(Long roleId);
}
