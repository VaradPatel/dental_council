package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SubDistrictValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SubDistrict {

    String message() default "Invalid Sub District, it must be only Alphabets";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
