package com.qinghua.website.api.config;

import com.qinghua.website.api.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors (InterceptorRegistry registry){
        registry.addInterceptor(jwtInterceptor())
                //拦截所有请求，通过判断token是否合法来决定是否需要登录
                .addPathPatterns("/app/**")
                //放开不需要拦截的请求
                .excludePathPatterns(
                        "/app/getTokenAPI",
                        "/app/getZLBTokenAPI",
                        "/app/logout",
                        "/app/registerCustomerAPI",
                        "/app/downloadContentAttachment"
                );
    }

    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }

}
