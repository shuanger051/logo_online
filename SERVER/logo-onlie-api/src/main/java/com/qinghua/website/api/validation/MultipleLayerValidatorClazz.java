package com.qinghua.website.api.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author yzhang
 * @date 2020/11/4
 * 多规则集合校验
 */
public class MultipleLayerValidatorClazz implements ConstraintValidator<MultipleLayerValidator, Object> {


    /**
     * 是否为空校验
     */
    boolean notBlank;

    /**
     * 是否为空校验失败返回的错误提示
     */
    String notBlankMessage;

    /**
     * 长度最小值
     *
     * @return
     */
    int min;

    /**
     * 长度最大值
     *
     * @return
     */
    int max;

    String lengthMessage;

    /**
     * 正则表达式
     *
     * @return
     */
    String regex;

    /**
     * 正则表达式校验失败返回错误提示信息
     */
    String regexMessage;

    @Override
    public void initialize(MultipleLayerValidator constraintAnnotation) {
        notBlank = constraintAnnotation.notBlank();
        notBlankMessage = constraintAnnotation.notBlankMessage();
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        lengthMessage = constraintAnnotation.lengthMessage();
        regex = constraintAnnotation.regex();
        regexMessage = constraintAnnotation.regexMessage();
    }

    @Override
    public boolean isValid(Object currentValue, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (notBlank) { // 校验是否为空
            if (currentValue == null || currentValue == "") {
                context.buildConstraintViolationWithTemplate(notBlankMessage).addConstraintViolation();
                return Boolean.FALSE;
            }
        }
        if (min != 0 || max != 9999) { // 校验长度
            int length = currentValue.toString().length();
            if (length < min || length > max) {
                if (lengthMessage.contains("{0}")) {
                    lengthMessage = lengthMessage.replace("{0}", String.valueOf(min));
                }
                if (max != 9999 && lengthMessage.contains("{1}")) {
                    lengthMessage = lengthMessage.replace("{1}", String.valueOf(max));
                } else if (max == 9999) {
                    lengthMessage = lengthMessage.replace("-{1}", "");
                }
                context.buildConstraintViolationWithTemplate(lengthMessage).addConstraintViolation();
                return Boolean.FALSE;
            }
        }
        if (StringUtils.isNoneBlank(regex)) { // 校验正则表达式
            if (!Pattern.matches(regex, currentValue.toString())) {
                context.buildConstraintViolationWithTemplate(regexMessage).addConstraintViolation();
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

}
