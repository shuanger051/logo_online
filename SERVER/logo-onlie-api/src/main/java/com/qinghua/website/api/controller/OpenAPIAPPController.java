package com.qinghua.website.api.controller;

import com.hazelcast.util.MD5Util;
import com.qinghua.website.api.annotation.LogAnnotation;
import com.qinghua.website.api.common.TokenTools;
import com.qinghua.website.api.controller.io.APPLoginIO;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.service.SysUserService;
import com.qinghua.website.server.utils.RSACryptoHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/app")
public class OpenAPIAPPController {

    @Autowired
    private SysUserService userService;

    /**
     * 用户登录APP获取TOKEN
     * @param loginIO
     * @param request
     * @return
     */
    @LogAnnotation(logType = "login",logDesc = "用户登录APP获取TOKEN")
    @RequestMapping("/getToken")
    public ResponseResult<Object> getToken(@Validated @RequestBody APPLoginIO loginIO, HttpServletRequest request){

        //先将前端传进来的RSA密码进行解密
        String password = RSACryptoHelper.decrypt(loginIO.getPassword());
        //将密码转为MD5
        String pwd = StringUtils.lowerCase(MD5Util.toMD5String(password));
        //根据账号密码生成TOKEN
        String token = TokenTools.genToken(loginIO.getUserName(),pwd);

        log.info("APP登录成功,返回token:{}",token);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",token);
        return ResponseResult.success(map);
    }

}
