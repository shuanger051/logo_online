package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.SysUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {

    /**
     * 根据ID查询管理员信息
     * @param sysUserDTO
     * @return
     */
    SysUserDTO getSysUser(SysUserDTO sysUserDTO);

    /**
     * 分页查询系统用户信息集合
     * @param bean
     * @return
     */
    public List<SysUserDTO> getSysUserList(SysUserDTO bean);

    /**
     * 根据用户名称&密码查询用户信息
     * @param bean
     * @return
     */
    public SysUserDTO getSysUserByNameAndPwd(SysUserDTO bean);

    /**
     * 系统用户登录校验
     * @param bean
     * @return
     */
    public SysUserDTO login(SysUserDTO bean);

    /**
     * 更新登录相关操作
     * @param bean
     */
    public void updateLoginSuccess(SysUserDTO bean);

    /**
     * 更新登录相关操作
     * @param bean
     */
    public void updateLoginFail(SysUserDTO bean);

    /**
     * 根据用户昵称检查用户是否存在
     * @param userName
     * @return
     */
    public SysUserDTO checkSysUserIsExist(@Param("userName") String userName);

    /**
     * 重置密码
     * @param resetPwd
     */
    public void resetPwd(SysUserDTO resetPwd);

    /**
     * 解除用户锁定状态
     * @param secureLock
     */
    public void secureAccountLock(SysUserDTO secureLock);

    /**
     * 新增系统用户信息
     * @param sysUserDTO
     */
    public void saveSysUser(SysUserDTO sysUserDTO);

    /**
     * 修改系统用户信息
     */
    public void updateUser(SysUserDTO sysUserDTO);

    /**
     * 修改系统用户密码
     * @param userName
     * @param password
     */
    public void changePwd(@Param("userName")String userName,@Param("password")String password);

    /**
     * 改变系统用户禁用状态
     * @param sysUserDTO
     */
    public void lockSysUser(SysUserDTO sysUserDTO);

    /**
     * 批量保存系统用户信息
     */
    public void saveSysUserByList(List<SysUserDTO> list);

    /**
     * 根据用户名称获取用户信息
     * @param userName
     * @return
     */
    public SysUserDTO getSysUserByUserName(String userName);

}
