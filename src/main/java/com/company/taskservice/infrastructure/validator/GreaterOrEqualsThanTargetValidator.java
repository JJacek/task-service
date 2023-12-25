package com.company.taskservice.infrastructure.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapperImpl;

public class GreaterOrEqualsThanTargetValidator
        implements ConstraintValidator<GreaterOrEqualsThanTarget, Object> {

    private String fieldGreaterOrEqual;

    private String fieldTarget;

    @Override
    public void initialize(GreaterOrEqualsThanTarget constraintAnnotation) {
        if (StringUtils.isBlank(constraintAnnotation.fieldGreaterOrEqual())) {
            throw new IllegalArgumentException("fieldGreaterOrEqual must be declared");
        }
        if (StringUtils.isBlank(constraintAnnotation.fieldTarget())) {
            throw new IllegalArgumentException("fieldTarget must be declared");
        }
        fieldGreaterOrEqual = constraintAnnotation.fieldGreaterOrEqual();
        fieldTarget = constraintAnnotation.fieldTarget();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl validatedObject = new BeanWrapperImpl(value);
        Object greaterOrEqual = validatedObject.getPropertyValue(fieldGreaterOrEqual);
        Object target = validatedObject.getPropertyValue(fieldTarget);

        if (greaterOrEqual != null && target != null) {
            Class<?> greaterOrEqualType = validatedObject.getPropertyType(fieldGreaterOrEqual);
            Class<?> targetType = validatedObject.getPropertyType(fieldTarget);

            if (CharSequence.class.isAssignableFrom(greaterOrEqualType) &&
                    CharSequence.class.isAssignableFrom(targetType)) {
                return ((CharSequence) greaterOrEqual).length() >= ((CharSequence) target).length();
            }

            throw new IllegalArgumentException("Validated types must be of type CharSequence");
        }

        return true;
    }

}
