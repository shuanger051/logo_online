package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.SysUserRoleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserRoleMapper {

    /**
     * 根据ID查询管理员角色关系信息
     * @param id
     * @return
     */
    SysUserRoleDTO getSysUserRoleById(Long id);

    /**
     * 查询管理员角色关系列表信息
     * @param sysUserRoleDTO
     * @return
     */
    List<SysUserRoleDTO> getSysUserRoleList(SysUserRoleDTO sysUserRoleDTO);

    /**
     * 保存管理员角色关系信息
     * @param sysUserRoleDTO
     */
    void saveSysUserRole(SysUserRoleDTO sysUserRoleDTO);

    /**
     * 更新管理员角色关系信息
     * @param sysUserRoleDTO
     */
    void updateSysUserRoleById(SysUserRoleDTO sysUserRoleDTO);

    /**
     * 根据ID删除管理员角色关系
     * @param id
     */
    void deleteSysUserRoleById(Long id);

    /**
     * 批量新增或修改用户角色信息
     * @param sysUserRoleDTOList
     * @return
     */
    int saveSysUserRoleForBatch(List<SysUserRoleDTO> sysUserRoleDTOList);

    /**
     * 根据UserID删除用户角色信息
     * @param sysUserRoleDTO
     */
    void deleteSysUserRoleByUserId(SysUserRoleDTO sysUserRoleDTO);

    /**
     * 批量删除用户角色信息
     * @param sysUserRoleDTOList
     */
    void deleteSysUserRoleByUserIdForBatch(List<SysUserRoleDTO> sysUserRoleDTOList);

    /**
     * 根据RoleID & UserId 查询UserRole信息
     * @param sysUserRoleDTO
     * @return
     */
    SysUserRoleDTO getSysUserRoleByUserIdANDRoleId(SysUserRoleDTO sysUserRoleDTO);

}
