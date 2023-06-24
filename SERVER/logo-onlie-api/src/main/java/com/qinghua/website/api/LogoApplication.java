package com.qinghua.website.api;

import com.qinghua.website.api.listener.CloseListener;
import com.qinghua.website.api.listener.StartListener;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.qinghua.website")
@EnableTransactionManagement
@EnableScheduling
@MapperScan(basePackages = "com.qinghua.website", annotationClass = Mapper.class)
public class LogoApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(LogoApplication.class);
        application.addListeners(new StartListener());
        application.addListeners(new CloseListener());
        application.run(args);
    }

}
