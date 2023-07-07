package com.qinghua.website.api.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.qinghua.website.api.annotation.LogAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 图片验证码
 */
@Slf4j
@RestController
@RequestMapping("/sys/img-code")
public class ImgCodeController {

    /**
     * 生成图片验证码
     * @param httpServletResponse
     * @throws Exception
     */
    @LogAnnotation(logType = "query",logDesc = "生成图片验证码")
    @GetMapping("/kaptcha")
    public void defaultKaptcha(HttpServletRequest request,HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/png");
        ShearCaptcha shearCaptcha= CaptchaUtil.createShearCaptcha(100, 30, 4, 0);
        // 验证码存入session
        request.getSession().setAttribute("imgVerifyCode", shearCaptcha.getCode());
        // 输出图片流
        shearCaptcha.write(httpServletResponse.getOutputStream());
    }

}
