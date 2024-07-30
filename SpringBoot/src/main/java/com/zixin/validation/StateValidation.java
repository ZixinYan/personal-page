package com.zixin.validation;
import com.zixin.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StateValidation implements ConstraintValidator<State,String> {
    /**
     *
     * @param s 传入的值
     * @param constraintValidatorContext
     * @return
     */

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if("已发布".equals(s) || "草稿".equals(s)){
            return true;
        }
        return false;
    }
}
