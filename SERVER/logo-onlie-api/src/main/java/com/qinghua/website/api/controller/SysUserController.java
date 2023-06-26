package com.qinghua.website.api.controller;

import com.github.pagehelper.PageInfo;
import com.hazelcast.util.MD5Util;
import com.hazelcast.util.Preconditions;
import com.qinghua.website.api.common.SessionUser;
import com.qinghua.website.api.controller.io.LoginIO;
import com.qinghua.website.api.controller.io.SysUserQueryIO;
import com.qinghua.website.api.controller.io.SysUserUpdateIO;
import com.qinghua.website.api.controller.vo.PageListVO;
import com.qinghua.website.api.controller.vo.SysUserVO;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.api.utils.IpUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.SysUserDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysUserService;
import com.qinghua.website.server.utils.RSACryptoHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseResult<Object> login(@Validated @RequestBody LoginIO loginIO, HttpServletRequest request){
        //校验验证码有效性 TODO
        String password = RSACryptoHelper.decrypt(loginIO.getPassword());
        return ResponseResult.success(userNameLogin(loginIO.getUserName(), password,request));
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
            //判定账户状态是否为锁定，锁定的账户不允许登录,且错误登录次数不得大于3次
            if(null != resUser.getIsDisabled() && resUser.getIsDisabled().equals("0") && resUser.getErrorCount() <= 3){
                log.info("[消息：] 用户{}登录成功",userName);
                //更新登录次数，最后登录时间，更新时间,错误次数为0
                SysUserDTO loginUpdateInfo = new SysUserDTO();
                loginUpdateInfo.setId(resUser.getId());
                loginUpdateInfo.setLastLoginIp(IpUtil.getRemoteAddr(request));
                loginUpdateInfo.setLoginCount(resUser.getLoginCount()+1);
                loginUpdateInfo.setLastLoginTime(new Date());
                sysUserService.updateLoginSuccess(loginUpdateInfo);
                //将用户数据写入session
                SessionUser user = new SessionUser();
                request.getSession().setAttribute(SessionUser.SEESION_USER,user);
                return Boolean.TRUE;
            }else{
                throw new BizException(SysConstant.LOGIN_ERROR_10002.getMsg(),SysConstant.LOGIN_ERROR_10002.getCode());
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
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ResponseResult<Object> logout(HttpServletRequest request){
        SessionUser user = (SessionUser) request.getSession().getAttribute(SessionUser.SEESION_USER);
        log.info("[消息：]用户{}正在执行退出操作!",user.getUserName());
        request.getSession().removeAttribute(SessionUser.SEESION_USER);
        request.getSession().invalidate();
        return ResponseResult.success("LogOut Success!");
    }

    @RequestMapping(value = "/updateSysUser",method = RequestMethod.POST)
    public ResponseResult<Object> updateUser(@Validated @RequestBody SysUserUpdateIO sysUserUpdateIO){
        return null;
    }

}
