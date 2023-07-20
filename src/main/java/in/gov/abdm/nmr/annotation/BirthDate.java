package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDate {
    String message() default "Invalid year of birthdate, date format must be MM-DD-YYYY";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
