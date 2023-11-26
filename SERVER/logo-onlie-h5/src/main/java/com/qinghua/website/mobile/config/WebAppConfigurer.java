package com.qinghua.website.mobile.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Value("${uploadPath.savePath}")
    private String savePath;   //图标物理存储路径

    @Value("${uploadPath.urlPath}")
    private String urlPath;   //图标映射路径

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(urlPath+"**").addResourceLocations("file:"+savePath);
    }

}
