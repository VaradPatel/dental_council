package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = AlphanumericValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Alphanumeric {
    String message() default "Invalid data. Data should be alphanumeric.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

