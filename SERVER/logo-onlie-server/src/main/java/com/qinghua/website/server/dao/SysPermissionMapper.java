package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.SysPermissionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysPermissionMapper {

    /**
     * 根据ID查询管理员权限信息
     * @param id
     * @return
     */
    SysPermissionDTO getSysPermissionById(Long id);

    /**
     * 在本组下根据ID查询管理员权限信息
     * @param sysPermissionDTO
     * @return
     */
    SysPermissionDTO getSysPermissionByIdAndGroup(SysPermissionDTO sysPermissionDTO);

    /**
     * 查询管理员权限列表信息
     * @param sysPermissionDTO
     * @return
     */
    List<SysPermissionDTO> getSysPermissionList(SysPermissionDTO sysPermissionDTO);

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
     * 根据ID删除管理员权限
     * @param id
     */
    void deleteSysPermissionById(Long id);

    /**
     * 根据用户ID获取权限信息
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
     * 根据权限名称获取权限信息
     * @param sysPermissionDTO
     * @return
     */
    SysPermissionDTO getPermsByName(SysPermissionDTO sysPermissionDTO);

    /**
     * 根据权限路径获取权限信息
     * @param sysPermissionDTO
     * @return
     */
    SysPermissionDTO getPermsByPath(SysPermissionDTO sysPermissionDTO);

    /**
     * 根据角色ID查询权限信息
     * @param id
     * @return
     */
    List<SysPermissionDTO> getPermissionByRoleId(Long id);

    /**
     * 通过分组获取全部权限列表
     * @param permissionGroup
     * @return
     */
    List<SysPermissionDTO> findAllListByGroup(@Param("permissionGroup") String permissionGroup);
}
