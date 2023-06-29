package com.qinghua.website.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = com.qinghua.website.api.validation.IdCardValidatorClazz.class)
public @interface IdCardValidator {

    String message() default "身份证号格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
