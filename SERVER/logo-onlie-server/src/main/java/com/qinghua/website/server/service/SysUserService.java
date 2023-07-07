package com.qinghua.website.server.service;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.SysUserDTO;

import java.util.List;

public interface SysUserService {

    /**
     * 分页查询系统用户信息集合
     * @param queryDTO
     * @return
     */
    public PageInfo<SysUserDTO> getSysUserList(SysUserDTO queryDTO);

    /**
     * 系统用户登录
     * @param loginUser
     * @return
     */
    public SysUserDTO login(SysUserDTO loginUser);

    /**
     * 更新系统用户登录操作信息
     * @return
     */
    public void updateLoginSuccess(SysUserDTO loginUpdate);

    /**
     * 更新系统用户登录操作信息
     * @param loginUpdate
     */
    public void updateLoginFail(SysUserDTO loginUpdate);

    /**
     * 根据用户昵称检查用户是否存在
     * @param userName
     * @return
     */
    public boolean checkSysUserIsExist(String userName);

    /**
     * 重置密码： 默认88888888
     * @param resetPwd
     */
    public void resetPwd(SysUserDTO resetPwd);

    /**
     * 解除系统用户锁定状态
     * @param lockBean
     */
    public void secureAccountLock(SysUserDTO lockBean);

    /**
     * 新增系统用户
     * @param sysUserDTO
     */
    public void saveSysUser(SysUserDTO sysUserDTO);

    /**
     * 修改系统用户信息
     * @param sysUserDTO
     */
    public void updateUser(SysUserDTO sysUserDTO);

    /**
     * 修改系统用户密码信息
     * @param userName
     * @param oldPassword
     * @param newPassword
     */
    public void changePwd(String userName,String oldPassword,String newPassword);

    /**
     * 改变系统用户禁用状态
     * @param sysUserDTO
     */
    public void lockSysUser(SysUserDTO sysUserDTO);

    /**
     * 批量保存系统用户信息
     * @param list
     */
    public void  saveSysUserByList(List<SysUserDTO> list);

    /**
     * 根据用户名称获取用户信息
     * @param userName
     * @return
     */
    SysUserDTO getSysUserByUserName(String userName);

}
