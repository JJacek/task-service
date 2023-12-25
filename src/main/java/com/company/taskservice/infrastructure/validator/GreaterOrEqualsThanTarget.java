package com.company.taskservice.infrastructure.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * At the moment we can use it only for {@link CharSequence}.
 */
@Constraint(validatedBy = GreaterOrEqualsThanTargetValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface GreaterOrEqualsThanTarget {

    String message() default "{constraints.GreaterOrEqualsThanTarget.message}";

    /**
     * The name of the field which value must be greater or equal then the value of the field {@link #fieldTarget()}.
     * At the moment we can use it only for {@link CharSequence}.
     *
     * @return the name of the field
     */
    String fieldGreaterOrEqual();

    /**
     * The name of the field which value must lower or equal then the value of the field {@link #fieldGreaterOrEqual()}.
     * At the moment we can use it only for {@link CharSequence}.
     *
     * @return the name of the field
     */
    String fieldTarget();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        GreaterOrEqualsThanTarget[] value();
    }

}
