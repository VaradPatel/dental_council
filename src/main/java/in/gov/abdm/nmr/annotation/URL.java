package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = URLValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface URL {
    String message() default "Please enter a valid Teleconsultation URL.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
