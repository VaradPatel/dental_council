package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotated interface for email validation
 */
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Target({FIELD})
public @interface Email {
    String message() default "Invalid email address.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean required() default false;
}