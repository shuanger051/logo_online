package com.qinghua.website.api.validation;

/**
 * @BelongsProject: inxvision-iot-server
 * @BelongsPackage: com.inxvision.iot.service.validation
 * @Author: 佰川
 * @CreateTime: 2020-10-21 10:29
 * @Description: 添加手机号校验注解
 */

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
 * 验证手机号，空和正确的手机号都能验证通过<br/>
 * 正确的手机号由11位数字组成，第一位为1
 * 第二位为 3、4、5、7、8
 *
 */
@ConstraintComposition(CompositionType.OR)
@Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}")
@Documented
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface MobilePhone {

    String message() default "手机号格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
