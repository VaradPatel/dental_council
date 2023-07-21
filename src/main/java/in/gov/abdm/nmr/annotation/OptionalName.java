package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OptionalNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalName {
    String message() default "Invalid input. it should be plain text.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
