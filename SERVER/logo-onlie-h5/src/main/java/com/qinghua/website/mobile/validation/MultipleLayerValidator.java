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
@Constraint(validatedBy = MultipleLayerValidatorClazz.class)
public @interface MultipleLayerValidator {

    /**
     * 是否为空
     * @return
     */
    boolean notBlank() default false;

    /**
     * 是否为空提示消息
     * @return
     */
    String notBlankMessage() default "参数不能为空";

    /**
     * 长度最小值
     * @return
     */
    int min() default 0;

    /**
     * 长度最大值
     * @return
     */
    int max() default 9999;

    String lengthMessage() default "长度为{0}-{1}之间";

    /**
     * 正则表达式
     * @return
     */
    String regex() default "";

    String regexMessage() default "不满足规则要求";

    Class<?>[] groups() default {};

    String message() default "";

    Class<? extends Payload>[] payload() default {};
}
