package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.SysRoleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    /**
     * 根据ID查询管理员角色信息
     * @param sysRoleDTO
     * @return
     */
    SysRoleDTO getSysRoleById(SysRoleDTO sysRoleDTO);

    /**
     * 查询管理员角色列表信息
     * @param sysRoleDTO
     * @return
     */
    List<SysRoleDTO> getSysRoleList(SysRoleDTO sysRoleDTO);

    /**
     * 保存管理员角色信息
     * @param sysRoleDTO
     */
    void saveSysRole(SysRoleDTO sysRoleDTO);

    /**
     * 更新管理员角色信息
     * @param sysRoleDTO
     */
    void updateSysRoleById(SysRoleDTO sysRoleDTO);

    /**
     * 根据ID删除管理员角色
     * @param sysRoleDTO
     */
    void deleteSysRoleById(SysRoleDTO sysRoleDTO);

    /**
     * 根据ID查询角色信息
     * @param userId
     * @return
     */
    List<String> getRoleListByUserId(Long userId);

    /**
     * 根据RoleName查询角色信息
     * @param sysRoleDTO
     * @return
     */
    SysRoleDTO getSysRoleByRoleName(SysRoleDTO sysRoleDTO);

    /**
     * 修改角色状态
     * @param sysRoleDTO
     * @return
     */
    void updateSysRoleStatusById(SysRoleDTO sysRoleDTO);
}
