package com.qinghua.website.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class IdCardValidatorClazz implements ConstraintValidator<IdCardValidator, Object> {

    //身份证最后一位的校验算法
    public static boolean verify(char[] id) {
        int sum = 0;
        int w[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        char[] ch = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
        for (int i = 0; i < id.length - 1; i++) {
            sum += (id[i] - '0') * w[i];
        }
        int c = sum % 11;
        char code = ch[c];
        char last = id[id.length-1];
        last = last == 'x' ? 'X' : last;
        return last == code;
    }

    @Override
    public boolean isValid(Object currentValue, ConstraintValidatorContext constraintValidatorContext) {
        String num = currentValue.toString();
        String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
        if (!num.matches(reg)) {
            return false;
        }
        char[] id = {};
        for (int i = 0; i < num.length(); i++) {
            id = Arrays.copyOf(id, id.length + 1);
            id[id.length - 1] = num.charAt(i);
        }
        boolean IsCorrect = verify(id);
        if (!IsCorrect) {
            return false;
        }
        return true;
    }

}
