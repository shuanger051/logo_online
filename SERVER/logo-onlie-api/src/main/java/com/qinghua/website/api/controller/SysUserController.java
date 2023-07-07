package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.hazelcast.util.MD5Util;
import com.hazelcast.util.Preconditions;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.common.SessionUser;
import com.qinghua.website.api.controller.io.*;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.controller.vo.SysUserVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.api.utils.IpUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.SysUserDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysUserService;
import com.qinghua.website.server.utils.DateUtil;
import com.qinghua.website.server.utils.RSACryptoHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;



    /**
     * 分页查询系统用户信息集合
     * @param sysUserQueryIO
     * @return
     */
    @LogAnnotation(logType = "query",logDesc = "分页查询系统用户信息集合")
    @GetMapping("/getSysUserList")
    public ResponseResult<Object> getSysUserList(@Validated @RequestBody SysUserQueryIO sysUserQueryIO){
        SysUserDTO queryDTO = BeanToolsUtil.copyOrReturnNull(sysUserQueryIO,SysUserDTO.class);
        PageInfo<SysUserDTO> pageList =  sysUserService.getSysUserList(queryDTO);
        List<SysUserVO> sysUserVOList =  BeanToolsUtil.copyAsList(pageList.getList(),SysUserVO.class);
        PageListVO<SysUserVO> resp = new PageListVO<>();
        resp.setList(sysUserVOList);
        resp.setTotal(pageList.getTotal());
        return ResponseResult.success(resp);
    }

    /**
     * 支持用戶名&密碼校驗登錄
     * @param loginIO 用户名+密码+验证码
     * @return
     */
    @LogAnnotation(logType = "login",logDesc = "支持用戶名&密碼校驗登錄")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseResult<Object> login(@Validated @RequestBody LoginIO loginIO, HttpServletRequest request){
        //校验验证码有效性，首先判断验证码是否生成，再判断验证码是否和session中相同
        if(null != request.getSession().getAttribute("imgVerifyCode")){
            String imgVerifyCode = request.getSession().getAttribute("imgVerifyCode").toString();
            if(null != imgVerifyCode && imgVerifyCode.equals(loginIO.getCaptchaCode())){
                //清除session中的图片验证码
                request.getSession().removeAttribute("imgVerifyCode");
                String password = RSACryptoHelper.decrypt(loginIO.getPassword());
                return ResponseResult.success(userNameLogin(loginIO.getUserName(), password,request));
            }else{
                throw new BizException(SysConstant.ERROR_IMG_CODE_IS_WRONG);
            }
        }else{
            throw new BizException(SysConstant.ERROR_MUST_CREATE_IMG_CODE_FIRST);
        }
    }

    /**
     * 登录擴展
     * @param userName
     * @param password
     * @return
     */
    public Boolean userNameLogin(String userName,String password, HttpServletRequest request){
        String pwd = StringUtils.lowerCase(MD5Util.toMD5String(password));
        SysUserDTO loginDTO = new SysUserDTO();
        loginDTO.setUserName(userName);
        loginDTO.setPassword(pwd);

        //校验账号是否存在
        boolean userCheck = sysUserService.checkSysUserIsExist(userName);
        Preconditions.checkTrue(userCheck, "账号信息非法!");

        //根据账号密码校验用户合法性
        SysUserDTO resUser = sysUserService.login(loginDTO);

        if(null != resUser){
            log.info("[消息:]用户{}正在执行登录操作",userName);

            Subject subject = SecurityUtils.getSubject();
            // 在认证提交前准备 token（令牌）
            UsernamePasswordToken token = new UsernamePasswordToken(resUser.getUserName(), MD5Util.toMD5String(password));


            //判定账户状态是否为锁定，锁定的账户不允许登录
            if(null != resUser.getIsDisabled() && resUser.getIsDisabled().equals("0")){
                //用户未锁定，追加判断错误次数校验，错误登录次数不得大于3次，否则不允许登录
                if(resUser.getErrorCount() <= 3){
                    log.info("[消息：] 用户{}登录成功",userName);
                    //更新登录次数，最后登录时间，更新时间,错误次数为0
                    SysUserDTO loginUpdateInfo = new SysUserDTO();
                    loginUpdateInfo.setId(resUser.getId());
                    loginUpdateInfo.setLastLoginIp(IpUtil.getRemoteAddr(request));
                    loginUpdateInfo.setLoginCount(resUser.getLoginCount()+1);
                    loginUpdateInfo.setLastLoginTime(new Date());
                    sysUserService.updateLoginSuccess(loginUpdateInfo);
                    //将用户数据写入session
                    SessionUser user = BeanToolsUtil.copyOrReturnNull(resUser,SessionUser.class);

                    // 执行认证登陆
                    subject.login(token);
                    //保存session
                    request.getSession().setAttribute(SessionUser.SEESION_USER,user);
                    return Boolean.TRUE;
                }else{
                    throw new BizException(SysConstant.ERROR_LOGIN_WRONG_MANY_TIMES);
                }
            }else{
                //用户已锁定，追加判断锁定时间是否已经超过24小时，若超过24小时则自动解除锁定
                Date curDate = DateUtil.formatDate(new Date());
                Date lockTime = resUser.getErrorTime();
                long cha = curDate.getTime() - lockTime.getTime();
                double result = cha * 1.0 / (1000 * 60 * 60);

                //小于24小时
                if(result<=24){
                    throw new BizException(SysConstant.LOGIN_ERROR_10002.getMsg(),SysConstant.LOGIN_ERROR_10002.getCode());
                }else{
                    log.info("[消息：] 用户{}登录成功",userName);
                    //更新登录次数，最后登录时间，更新时间,错误次数为0
                    SysUserDTO loginUpdateInfo = new SysUserDTO();
                    loginUpdateInfo.setId(resUser.getId());
                    loginUpdateInfo.setLastLoginIp(IpUtil.getRemoteAddr(request));
                    loginUpdateInfo.setLoginCount(resUser.getLoginCount()+1);
                    loginUpdateInfo.setLastLoginTime(new Date());
                    sysUserService.updateLoginSuccess(loginUpdateInfo);
                    //将用户数据写入session
                    SessionUser user = BeanToolsUtil.copyOrReturnNull(resUser,SessionUser.class);

                    // 执行认证登陆
                    subject.login(token);
                    //保存session
                    request.getSession().setAttribute(SessionUser.SEESION_USER,user);
                    return Boolean.TRUE;
                }
            }
        }else{
            log.info("[消息：] 用户{}用户密码不匹配,错误次数为{}",resUser.getUserName(),resUser.getErrorCount()+1);
            //账号密码不合法，更新错误次数，IP,登录次数，最后登录时间，更新时间,错误次数为0，若错误次数达到3次则同时将账户状态变更为锁定,
            SysUserDTO loginUpdateInfo = new SysUserDTO();
            loginUpdateInfo.setId(resUser.getId());
            loginUpdateInfo.setLastLoginIp(IpUtil.getRemoteAddr(request));
            loginUpdateInfo.setLoginCount(resUser.getLoginCount()==null? 1 :resUser.getLoginCount()+1);
            loginUpdateInfo.setLastLoginTime(new Date());
            loginUpdateInfo.setErrorCount(resUser.getErrorCount()+1);
            loginUpdateInfo.setErrorTime(new Date());
            loginUpdateInfo.setErrorIp(IpUtil.getRemoteAddr(request));

            if(resUser.getErrorCount()+1==3){
                loginUpdateInfo.setIsDisabled("1");
            }

            sysUserService.updateLoginFail(loginUpdateInfo);
            return Boolean.FALSE;
        }
    }

    /**
     * 系统用户登出
     * @param request
     * @return
     */
    @LogAnnotation(logType = "logout",logDesc = "系统用户登出")
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ResponseResult<Object> logout(HttpServletRequest request){
        SessionUser user = (SessionUser) request.getSession().getAttribute(SessionUser.SEESION_USER);
        log.info("[消息：]用户{}正在执行退出操作!",user.getUserName());
        request.getSession().removeAttribute(SessionUser.SEESION_USER);
        request.getSession().invalidate();
        return ResponseResult.success("LogOut Success!");
    }

    /**
     * 更新系统用户信息
     * @param sysUserUpdateIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "更新系统用户信息")
    @RequestMapping(value = "/updateSysUser",method = RequestMethod.POST)
    public ResponseResult<Object> updateUser(@Validated @RequestBody SysUserUpdateIO sysUserUpdateIO){
        SysUserDTO sysUserDTO = BeanToolsUtil.copyOrReturnNull(sysUserUpdateIO,SysUserDTO.class);
        sysUserService.updateUser(sysUserDTO);
        return ResponseResult.success();
    }

    /**
     * 新增系统用户信息
     * @param sysUserSaveIO
     * @return
     */
    @LogAnnotation(logType = "save",logDesc = "新增系统用户信息")
    @RequestMapping(value = "/saveSysUser",method = RequestMethod.POST)
    public ResponseResult<Object> saveUser(@Validated @RequestBody SysUserSaveIO sysUserSaveIO,HttpServletRequest request){
        SysUserDTO sysUserDTO = BeanToolsUtil.copyOrReturnNull(sysUserSaveIO,SysUserDTO.class);
        sysUserDTO.setPassword(MD5Util.toMD5String(sysUserDTO.getPassword()));
        sysUserDTO.setLoginCount(0);
        sysUserDTO.setIsDisabled("0");
        sysUserDTO.setIsAdmin("0");
        sysUserDTO.setActivation("1");
        sysUserDTO.setRegisterIp(IpUtil.getRemoteAddr(request));
        sysUserService.saveSysUser(sysUserDTO);
        return ResponseResult.success();
    }

    /**
     * 重置系统用户密码
     * @param sysUserUpdatePWDIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "重置系统用户密码")
    @RequestMapping(value = "/resetPwd",method = RequestMethod.POST)
    public ResponseResult<Object> resetPwd(@Validated @RequestBody SysUserUpdatePWDIO sysUserUpdatePWDIO,HttpServletRequest request){
        //判断当前操作用户是否为超级管理员，只有超管才可以访问该接口
        SessionUser user = (SessionUser) request.getSession().getAttribute(SessionUser.SEESION_USER);
        if(null != user){
            if(user.getIsAdmin().equals("1")){
                SysUserDTO sysUserDTO = BeanToolsUtil.copyOrReturnNull(sysUserUpdatePWDIO,SysUserDTO.class);
                sysUserService.resetPwd(sysUserDTO);
                return ResponseResult.success();
            }else{
                throw new BizException(SysConstant.ERROR_NOLY_SUPER_ADMIN_CAN_DO);
            }
        }else{
            throw new BizException(SysConstant.ERROR_USER_NOT_LOGIN);
        }
    }

    /**
     * 超管解除账号锁定状态
     * @param sysUserLockIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "超管解除账号锁定状态")
    @RequestMapping(value = "/secureAccountLock",method = RequestMethod.POST)
    public ResponseResult<Object> secureAccountLock(@Validated @RequestBody SysUserLockIO sysUserLockIO, HttpServletRequest request){
        //判断当前操作用户是否为超级管理员，只有超管才可以访问该接口
        SessionUser user = (SessionUser) request.getSession().getAttribute(SessionUser.SEESION_USER);
        if(null != user){
            if(user.getIsAdmin().equals("1")){
                SysUserDTO sysUserDTO = BeanToolsUtil.copyOrReturnNull(sysUserLockIO,SysUserDTO.class);
                sysUserService.secureAccountLock(sysUserDTO);
                return ResponseResult.success();
            }else{
                throw new BizException(SysConstant.ERROR_NOLY_SUPER_ADMIN_CAN_DO);
            }
        }else{
            throw new BizException(SysConstant.ERROR_USER_NOT_LOGIN);
        }
    }

    /**
     * 系统用户修改密码
     * @param sysUserPwdIO
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "系统用户修改密码")
    @RequestMapping(value = "/changePwd",method = RequestMethod.POST)
    @RequiresPermissions("/sys/user/changePwd")
    public ResponseResult<Object> changePwd(@Validated @RequestBody SysUserPwdIO sysUserPwdIO){
        sysUserService.changePwd(sysUserPwdIO.getUserName(),sysUserPwdIO.getOldPassword(),sysUserPwdIO.getNewPassword());
        return ResponseResult.success();
    }

    /**
     * 改变系统用户禁用状态
     * @param sysUserStatusIO
     * @param request
     * @return
     */
    @LogAnnotation(logType = "update",logDesc = "改变系统用户禁用状态")
    @RequestMapping(value = "/lockSysUser",method = RequestMethod.POST)
    public ResponseResult<Object> lockSysUser(@Validated @RequestBody SysUserStatusIO sysUserStatusIO, HttpServletRequest request){
        //判断当前操作用户是否为超级管理员，只有超管才可以访问该接口
        SessionUser user = (SessionUser) request.getSession().getAttribute(SessionUser.SEESION_USER);
        if(null != user){
            if(user.getIsAdmin().equals("1")){
                SysUserDTO sysUserDTO = BeanToolsUtil.copyOrReturnNull(sysUserStatusIO,SysUserDTO.class);
                sysUserService.lockSysUser(sysUserDTO);
                return ResponseResult.success();
            }else{
                throw new BizException(SysConstant.ERROR_NOLY_SUPER_ADMIN_CAN_DO);
            }
        }else{
            throw new BizException(SysConstant.ERROR_USER_NOT_LOGIN);
        }
    }


}
