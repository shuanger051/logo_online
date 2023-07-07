package com.qinghua.website.api.filter;

import com.alibaba.fastjson.JSON;
import com.qinghua.website.api.utils.UserUtil;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 配置shiro未登录异常处理
 */
@Slf4j
public class ShiroLoginFilter extends FormAuthenticationFilter {

    /**
     * 当shiro校验用户未登录时，返回JSON数据代替原有的跳转到登录界面
     * @param servletRequest
     * @param servletResponse
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();

        String json = JSON.toJSONString(ResponseResult.error(SysConstant.ERROR_USER_NOT_LOGIN));

        out.write(json);
        out.flush();
        out.close();
        return false;
    }

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        // 判断是否有用户数据，如果有的话则删除当前线程里面的用户信息，否则在使用线程池的时候会有问题
        if (UserUtil.getUser() != null) {
            UserUtil.removeUser();
        }
    }
}
