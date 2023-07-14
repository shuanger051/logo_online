package com.qinghua.website.api.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.SysUserDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private SysUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)   {
        String token = request.getHeader("token");
        //如果不映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        // 执行认证
        if (StrUtil.isBlank(token)) {
            throw new BizException("无token信息,请先登录获取Token!",SysConstant.SYSTEM_ERROR_401.getCode());
        }
        // 获取 token 中的userName
        String userName;
        try {
            userName = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new BizException("Token验证失败!",SysConstant.SYSTEM_ERROR_401.getCode());
        }
        //根据token中的userName查询数据库
        SysUserDTO user = userService.getSysUserByUserName(userName);
        if (user == null) {
            throw new BizException("用户不存在,请重新登录!",SysConstant.SYSTEM_ERROR_401.getCode());
        }

        // 用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new BizException("Token验证失败,请重新登录!",SysConstant.SYSTEM_ERROR_401.getCode());
        }
        return true;
    }

}
