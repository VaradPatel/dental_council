package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VillageValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Village {

    String message() default "Invalid village, it must be only alphabets";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
