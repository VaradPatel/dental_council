package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegistrationNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegistrationNumber {
    String message() default "Invalid RegistrationNumber.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
