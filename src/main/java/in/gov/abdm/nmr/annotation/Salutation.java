package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SalutationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Salutation {
    String message() default "Invalid Salutation, it should be Dr.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
