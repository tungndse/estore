package com.coldev.estore.config.validation;

import com.coldev.estore.config.validation.implementation.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface UsernameValidation {
    String message() default """
            Username requirements:
            Username consists of alphanumeric characters (a-zA-Z0-9), lowercase, or uppercase.
            Username allowed of the dot (.), underscore (_), and hyphen (-).
            The dot (.), underscore (_), or hyphen (-) must not be the first or last character.
            The dot (.), underscore (_), or hyphen (-) does not appear consecutively, e.g., java..regex
            The number of characters must be between 5 to 20.""";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

