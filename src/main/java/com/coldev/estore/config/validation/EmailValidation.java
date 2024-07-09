package com.coldev.estore.config.validation;

import com.coldev.estore.config.validation.implementation.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface EmailValidation {

    String message() default "Email is not in the correct format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
