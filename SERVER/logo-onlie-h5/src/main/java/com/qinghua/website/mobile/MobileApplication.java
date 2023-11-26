package com.qinghua.website.mobile;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.qinghua.website")
@EnableTransactionManagement
@EnableScheduling
@MapperScan(basePackages = "com.qinghua.website", annotationClass = Mapper.class)
public class MobileApplication {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(MobileApplication.class, args);
    }

    /**
     * 重启服务
     */
    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);
        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(MobileApplication.class, args.getSourceArgs());
        });
        // 设置非守护线程
        thread.setDaemon(false);
        thread.start();
    }

}
