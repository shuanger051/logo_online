package com.qinghua.website.mobile.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author yzhang
 * @date 2020/11/4
 * @desc 数据字典入参校验
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = DictValidatorClazz.class)
public @interface DictValidator {

    // 枚举里面的Code
    String value();

    // 默认提示内容
    String message() default "数据字典不存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
