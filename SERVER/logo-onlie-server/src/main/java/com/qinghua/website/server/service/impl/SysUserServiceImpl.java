package com.qinghua.website.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hazelcast.util.MD5Util;
import com.hazelcast.util.Preconditions;
import com.qinghua.website.server.config.SysConfig;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.dao.SysUserMapper;
import com.qinghua.website.server.domain.SysUserDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 重置密码：默认8个8
     * @param resetPwd
     */
    @Override
    public void resetPwd(SysUserDTO resetPwd){
        resetPwd.setPassword(MD5Util.toMD5String(SysConfig.INIT_PWD));
        sysUserMapper.resetPwd(resetPwd);
    }

    /**
     * 解除用户锁定状态
     * @param lockBean
     */
    @Override
    public void secureAccountLock(SysUserDTO lockBean){
        sysUserMapper.secureAccountLock(lockBean);
    }

    /**
     * 新增系统用户
     * @param sysUserDTO
     */
    @Override
    public void saveSysUser(SysUserDTO sysUserDTO){
        //校验用户名是否已存在，若已存在则提示重复
        SysUserDTO checkUserName = sysUserMapper.checkSysUserIsExist(sysUserDTO.getUserName());
        if(null != checkUserName){
            throw new BizException(SysConstant.ERROR_USER_REPEAT_USERNAME);
        }
        sysUserMapper.saveSysUser(sysUserDTO);
    }

    /**
     * 修改系统用户信息
     * @param sysUserDTO
     */
    @Override
    public void updateUser(SysUserDTO sysUserDTO){
        sysUserMapper.updateUser(sysUserDTO);
    }

    /**
     * 修改系统用户密码信息
     * @param userName
     * @param oldPassword
     * @param newPassword
     */
    @Override
    public void changePwd(String userName,String oldPassword,String newPassword){
        //校验用户名称是否合法
        SysUserDTO checkUserName = sysUserMapper.checkSysUserIsExist(userName);
        if(null != checkUserName){
            //校验旧密码是否合法
            SysUserDTO query = new SysUserDTO();
            query.setUserName(userName);
            query.setPassword(MD5Util.toMD5String(oldPassword));
            SysUserDTO sysUserDTO = sysUserMapper.getSysUserByNameAndPwd(query);
            if(null != sysUserDTO){
                sysUserMapper.changePwd(userName,MD5Util.toMD5String(newPassword));
            }else{
                throw new BizException(SysConstant.ERROR_USER_CHECK_OLD_PWD);
            }
        }else{
            throw new BizException(SysConstant.ERROR_SYS_USER_NOT_EXISTS);
        }
    }

    /**
     * 改变系统用户禁用状态
     * @param sysUserDTO
     */
    public void lockSysUser(SysUserDTO sysUserDTO){
        //校验用户名称是否合法
        SysUserDTO checkUserName = sysUserMapper.checkSysUserIsExist(sysUserDTO.getUserName());
        if(null != checkUserName){
            sysUserMapper.lockSysUser(sysUserDTO);
        }else{
            throw new BizException(SysConstant.ERROR_SYS_USER_NOT_EXISTS);
        }
    }

    /**
     *
     * @param list
     */
    @Override
    public void saveSysUserByList(List<SysUserDTO> list){
        if(null != list){
            try {
                sysUserMapper.saveSysUserByList(list);
            }catch (Exception e) {
                Throwable cause = e.getCause();
                if(cause instanceof java.sql.SQLIntegrityConstraintViolationException){
                    throw new BizException(SysConstant.ERROR_USER_REPEAT_USERNAME);
                }
            }
        }else{
            //不允许上传空表格
            throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
        }
    }

    /**
     * 根据用户名称获取用户信息
     * @param userName
     * @return
     */
    @Override
    public SysUserDTO getSysUserByUserName(String userName){
        return sysUserMapper.getSysUserByUserName(userName);
    }

}
