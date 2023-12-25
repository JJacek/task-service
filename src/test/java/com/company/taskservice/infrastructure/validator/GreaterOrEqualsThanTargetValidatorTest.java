package com.company.taskservice.infrastructure.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GreaterOrEqualsThanTargetValidatorTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    void validSomeRecord_notNullFieldsValue() {
        SomeRecord someRecord = new SomeRecord("ABCD", "BCD");

        Set<ConstraintViolation<SomeRecord>> violations = validator.validate(someRecord);
        assertThat(violations).isEmpty();
    }

    @Test
    void validSomeRecord_nullFieldsValue() {
        SomeRecord someRecord = new SomeRecord(null, null);

        Set<ConstraintViolation<SomeRecord>> violations = validator.validate(someRecord);
        assertThat(violations).isEmpty();
    }

    @Test
    void notValidSomeRecord_inputIsShorterThenPattern() {
        SomeRecord someRecord = new SomeRecord("AB", "BCD");

        Set<ConstraintViolation<SomeRecord>> violations = validator.validate(someRecord);
        assertThat(violations).hasSize(1);
    }

    @GreaterOrEqualsThanTarget(fieldGreaterOrEqual = "input", fieldTarget = "pattern")
    record SomeRecord(String input, String pattern) {}

}