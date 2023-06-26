package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.SysRolePermissionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRolePermissionMapper {

    /**
     * 根据ID查询角色权限关系信息
     * @param id
     * @return
     */
    SysRolePermissionDTO getSysRolePermissionById(Long id);

    /**
     * 查询角色权限关系列表信息
     * @param sysRolePermissionDTO
     * @return
     */
    List<SysRolePermissionDTO> getSysRolePermissionList(SysRolePermissionDTO sysRolePermissionDTO);

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
     * 根据RoleID批量删除权限信息
     * @param sysRolePermissionDTOList
     */
    void deleteSysRolePermissionByUserIdForBatch(List<SysRolePermissionDTO> sysRolePermissionDTOList);

    /**
     * 根据RoleId & PermissionId查询角色权限信息
     * @param sysRolePermissionDTO
     * @return
     */
    SysRolePermissionDTO getSysRolePermsByRoleIdANDPermsId(SysRolePermissionDTO sysRolePermissionDTO);

    /**
     * 根据角色ID查询角色权限
     */
    List<SysRolePermissionDTO> getSysRolePermissionListByRoleId(Long roleId);
}
