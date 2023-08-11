package com.project.Project.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum,String> {

    private ValidEnum annotation;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return false;
        boolean result = false;
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if(enumValues != null){
            for(Object enumValue: enumValues){
                if(value.equals(enumValue.toString()) || (this.annotation.ignoreCase() && value.equalsIgnoreCase(enumValue.toString()))){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
