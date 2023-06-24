package com.qinghua.website.server.dao;

import com.qinghua.website.server.domain.SysUserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {

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

}
