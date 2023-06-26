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

}
