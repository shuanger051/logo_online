package com.qinghua.website.mobile.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {IsJsonConstraintValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsJson {

    String message() default "非法格式的JSON字符串";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
