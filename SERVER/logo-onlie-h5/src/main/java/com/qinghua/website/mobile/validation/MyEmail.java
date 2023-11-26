package com.qinghua.website.mobile.validation;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @BelongsProject: inxvision-iot-server
 * @BelongsPackage: com.inxvision.iot.service.validation
 * @Author: 佰川
 * @CreateTime: 2020-11-13 15:06
 * @Description: email校验注解
 */
@ConstraintComposition(CompositionType.OR)
@Pattern(regexp = "^[a-zA-Z0-9]([a-zA-Z0-9_.-]+)@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")
@Documented
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface MyEmail {

    String message() default "邮箱格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
