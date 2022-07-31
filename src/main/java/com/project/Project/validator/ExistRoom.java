package com.project.Project.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = BuildingExistValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistRoom {
    String message() default "해당하는 방이 없습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}


