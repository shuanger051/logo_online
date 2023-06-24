package com.qinghua.website.server.service;

import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.domain.SysUserDTO;

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

}
