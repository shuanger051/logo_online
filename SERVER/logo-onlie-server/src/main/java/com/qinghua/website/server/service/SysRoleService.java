package com.qinghua.website.server.service;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.domain.SysRoleDTO;

import java.util.List;

/**
 * @ClassName SysRoleService
 * @Description 系统角色管理Service层
 **/
public interface SysRoleService {

    /**
     * 根据ID查询管理员角色信息
     * @param id
     * @return
     */
    SysRoleDTO getSysRoleById(Long id);

    /***
     * 查询管理员角色信息集合
     * @param sysRoleDTO
     * @return
     */
    List<SysRoleDTO> getSysRoleList(SysRoleDTO sysRoleDTO);

    /**
     * 分页查询角色信息集合
     * @param sysRoleDTO
     * @return
     */
    PageInfo<SysRoleDTO> getSysRoleListByPage(SysRoleDTO sysRoleDTO);

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
     * 根据ID删除系统管理员角色
     * @param sysRoleDTO
     */
    void deleteSysRoleById(SysRoleDTO sysRoleDTO);

    /**
     * 根据用户ID查询角色信息
     * @param userId
     * @return
     */
    List<String> getRoleListByUserId(Long userId);

    /**
     * 修改角色状态
     * @param sysRoleDTO
     * @return
     */
    void updateSysRoleStatusById(SysRoleDTO sysRoleDTO);
}
