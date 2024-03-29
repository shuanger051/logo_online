package com.qinghua.website.mobile.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.domain.CustomerInfoDTO;
import com.qinghua.website.server.exception.BizException;
import com.qinghua.website.server.service.CustomerInfoService;
import com.qinghua.website.server.utils.DateUtil;
import com.qinghua.website.server.utils.RSACoder;
import com.qinghua.website.server.utils.RSACryptoHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private CustomerInfoService customerInfoService;
    
    @Value("${sys.config.qlyg_power}")
    private String SYS_QLYG_POWER;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)   {

        String token = request.getParameter("token");

        // 执行认证
        if (StrUtil.isBlank(token)) {
            throw new BizException("无token信息,请先登录获取Token!",SysConstant.SYSTEM_ERROR_401.getCode());
        }

        //如果不映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        //如果是权利阳光的系统配置，则单独处理token
        if(null != SYS_QLYG_POWER && "true".equals(SYS_QLYG_POWER)){

            try{

                LocalDateTime timeStr = DateUtil.fromTimeStamp(Long.valueOf(JWT.decode(token).getAudience().get(0)));
                LocalDateTime currentTime = LocalDateTime.now();
                long minutesDifference = ChronoUnit.MINUTES.between(timeStr, currentTime);
                if(minutesDifference > 5){
                    throw new BizException("Token已失效,验证失败!",SysConstant.SYSTEM_ERROR_401.getCode());
                }
            } catch (JWTDecodeException j){
                throw new BizException("Token验证失败!",SysConstant.SYSTEM_ERROR_401.getCode());
            }

            // 用户密码加签验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(JWT.decode(token).getAudience().get(0))).build();
            try{
                jwtVerifier.verify(token); // 验证token
            } catch (JWTVerificationException e) {
                throw new BizException("Token验证失败,请重新登录!",SysConstant.SYSTEM_ERROR_401.getCode());
            }
            return true;

        }else{
            // 获取 token 中的customerName
            String customerName;
            try {
                customerName = JWT.decode(token).getAudience().get(0);
            } catch (JWTDecodeException j) {
                throw new BizException("Token验证失败!",SysConstant.SYSTEM_ERROR_401.getCode());
            }
            //根据token中的userName查询数据库
            CustomerInfoDTO customer = customerInfoService.getCustomerByCustomerName(customerName);
            if (customer == null) {
                throw new BizException("用户不存在,请重新登录!",SysConstant.SYSTEM_ERROR_401.getCode());
            }

            // 用户密码加签验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(customer.getPassword())).build();
            try{
                jwtVerifier.verify(token); // 验证token
            } catch (JWTVerificationException e) {
                throw new BizException("Token验证失败,请重新登录!",SysConstant.SYSTEM_ERROR_401.getCode());
            }
            return true;
        }

    }

}
