package com.qinghua.website.mobile.validation;

import com.qinghua.website.server.domain.SysDictItemDTO;
import com.qinghua.website.server.utils.DictUtil;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Set;

/**
 * @author yzhang
 * @date 2020/11/4
 * 字典数据字典具体校验类
 */
public class DictValidatorClazz implements ConstraintValidator<DictValidator, Object> {

    @Resource
    private DictUtil dictUtil;

    private String code;

    @Override
    public void initialize(DictValidator constraintAnnotation) {
        code = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object currentValue, ConstraintValidatorContext constraintValidatorContext) {
        if (null == currentValue || "" == currentValue)
            return true;
        SysDictItemDTO sysDictItemDTO = new SysDictItemDTO();
        sysDictItemDTO.setDictKey(code);
        Map<String, Object> map = dictUtil.getDictItemsByDictKey(code);
        Set<String> dictItemKeys = map.keySet();
        return dictItemKeys.contains(currentValue);
    }
}
