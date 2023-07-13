package com.qinghua.website.api.filter;

import com.alibaba.fastjson.JSON;
import com.qinghua.website.server.common.ResponseResult;
import com.qinghua.website.server.constant.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 配置未授权返回JSON
 */
@Slf4j
public class ShiroAuthorizationFilter extends RolesAuthorizationFilter {

    /**
     * 校验用户权限，当无权限时返回JSON数据代替原有的跳转到界面
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        Subject subject = getSubject(servletRequest, servletResponse);

        String json;
        if (subject.getPrincipal() == null) {
            // 没有认证,先返回未认证的json
            json = JSON.toJSONString(ResponseResult.error(SysConstant.ERROR_USER_UN_LOGIN));
        } else {
            // 已认证但没有角色，返回为授权的json
            json = JSON.toJSONString(ResponseResult.error(SysConstant.ERROR_USER_NO_RIGHTS));
        }

        out.write(json);
        out.flush();
        out.close();
        return false;
    }

}
