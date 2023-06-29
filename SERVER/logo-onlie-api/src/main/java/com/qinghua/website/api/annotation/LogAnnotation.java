package com.qinghua.website.api.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface LogAnnotation {

    String logType() default "";

    String logDesc() default "";

}
