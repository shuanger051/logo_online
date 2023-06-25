package com.qinghua.website.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qinghua.website.server.dao.SysUserMapper;
import com.qinghua.website.server.domain.SysUserDTO;
import com.qinghua.website.server.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 分页查询系统用户信息
     * @param queryBean
     * @return
     */
    @Override
    public PageInfo<SysUserDTO> getSysUserList(SysUserDTO queryBean){
        PageHelper.startPage(queryBean.getPageNum(), queryBean.getPageSize());
        List<SysUserDTO> resList = sysUserMapper.getSysUserList(queryBean);
        return new PageInfo<>(resList);
    }

    /**
     * 系统用户登录
     * @param login
     * @return
     */
    @Override
    public SysUserDTO login(SysUserDTO login){
        return sysUserMapper.login(login);
    }

    /**
     * 更新登录相关信息
     * @return
     */
    @Override
    public void updateLoginSuccess(SysUserDTO updateLogin){
        sysUserMapper.updateLoginSuccess(updateLogin);
    }

    /**
     * 更新登录相关信息
     * @param updateLogin
     */
    @Override
    public void updateLoginFail(SysUserDTO updateLogin){
        sysUserMapper.updateLoginFail(updateLogin);
    }

    /**
     * 根据用户昵称检查用户是否存在
     * @param userName
     * @return
     */
    @Override
    public boolean checkSysUserIsExist(String userName){
        boolean checkFlag =  sysUserMapper.checkSysUserIsExist(userName) == null ? false:true;
        return checkFlag;
    }

}
