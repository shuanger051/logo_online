package com.qinghua.website.api.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.qinghua.website.api.utils.BeanToolsUtil;
import com.qinghua.website.server.domain.SysUserDTO;
import com.qinghua.website.server.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenTools {

    private static SysUserService staticUserService;

    @Autowired
    private SysUserService userService;

    @PostConstruct
    public void setUserService(){
        staticUserService = userService;
    }

    //生成token
    public static String genToken(String userName, String password) {
        return JWT.create().withAudience(userName) // 将 userName 保存到 token 里面,作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2小时后token过期
                .sign(Algorithm.HMAC256(password)); // 以 password 作为 token 的密钥
    }

    //获取当前用户对象信息
    public static SessionUser getCurrentUser(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)){
                String userName = JWT.decode(token).getAudience().get(0);
                SysUserDTO userDTO = staticUserService.getSysUserByUserName(userName);
                return BeanToolsUtil.copyOrReturnNull(userDTO,SessionUser.class);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}