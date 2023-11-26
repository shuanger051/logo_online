package com.qinghua.website.mobile.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String allowOrigin = httpRequest.getHeader("Origin");
        String referer = httpRequest.getHeader("Referer");
        System.out.println("AllowOrigin:" + allowOrigin + "Referer:" + referer);

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", allowOrigin == null?referer:allowOrigin);
        httpResponse.setHeader("Access-Control-Allow-Method","POST, GET, OPTIONS, DELETE");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,Authorization,Token");
        httpResponse.setHeader("Access-Control-Max-Age", "1800");
        httpResponse.setHeader("Access-Control-Expose-Headers", "*");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {}
    @Override
    public void destroy() {}

}
