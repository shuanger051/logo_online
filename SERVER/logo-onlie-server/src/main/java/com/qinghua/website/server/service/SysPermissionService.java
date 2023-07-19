package com.qinghua.website.server.service;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.domain.SysPermissionDTO;

import java.util.List;

/**
 * @ClassName SysPermissionService
 * @Description 系统权限管理Service层
 **/
public interface SysPermissionService {

    /**
     * 根据用户ID查询管理员权限信息
     * @param id
     * @return
     */
    SysPermissionDTO getSysPermissionById(Long id);

    /***
     * 查询管理员权限信息集合
     * @param sysPermissionDTO
     * @return
     */
    List<SysPermissionDTO> getSysPermissionList(SysPermissionDTO sysPermissionDTO);

    /***
     * 分页查询管理员权限信息集合
     * @param sysPermissionDTO
     * @return
     */
    PageInfo<SysPermissionDTO> getSysPermissionListByPage(SysPermissionDTO sysPermissionDTO);


    /**
     * 保存管理员权限信息
     * @param sysPermissionDTO
     */
    void saveSysPermission(SysPermissionDTO sysPermissionDTO);

    /**
     * 更新管理员权限信息
     * @param sysPermissionDTO
     */
    void updateSysPermissionById(SysPermissionDTO sysPermissionDTO);

    /**
     * 根据ID删除系统管理员权限
     * @param id
     */
    void deleteSysPermissionById(Long id);

    /**
     * 根据用户ID查询权限信息
     * @param userId
     * @return
     */
    List<String> getPermsStringByUserId(Long userId);

    /**
     * 根据用户ID查询权限集合
     * @param userId
     * @return
     */
    List<SysPermissionDTO> getPermsListByUserId(Long userId);

    /**
     * 根据角色ID查询权限信息
     * @param id
     * @return
     */
    List<SysPermissionDTO> getPermissionByRoleId(Long id);

    /**
     * 通过分组获取全部权限
     * @param code
     * @return
     */
    List<SysPermissionDTO> findAllListByGroup(String code);

}
