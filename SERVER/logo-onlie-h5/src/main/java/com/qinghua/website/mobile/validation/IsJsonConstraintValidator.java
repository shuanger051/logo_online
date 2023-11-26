package com.qinghua.website.mobile.validation;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsJsonConstraintValidator implements ConstraintValidator<IsJson, String> {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void initialize(IsJson constraintAnnotation) {
        //TODO 这里可以做引用值传递，如果需要将注解注入的字段值获取，可在这里进行接收，取值方式：String value = constraintAnnotation.value()
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isEmpty(value) || isJsonValid(value);
    }

    /**
     * 利用fastjson的readTree方法判断json是否合法
     * @param jsonInString
     * @return
     */
    public static boolean isJsonValid(String jsonInString) {
        if(StringUtils.isEmpty(jsonInString)){
            return false;
        }
        boolean isJsonObject = true;
        boolean isJsonArray = true;
        try {
            JSONObject.parseObject(jsonInString);
        } catch (Exception e) {
            isJsonObject = false;
        }
        try {
            JSONObject.parseArray(jsonInString);
        } catch (Exception e) {
            isJsonArray = false;
        }
        if(!isJsonObject && !isJsonArray){ //不是json格式
            return false;
        }
        return true;
    }

}
