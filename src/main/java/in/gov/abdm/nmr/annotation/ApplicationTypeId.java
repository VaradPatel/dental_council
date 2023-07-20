package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ApplicationTypeIdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationTypeId {
    String message() default "Invalid application type id. supportive actions are 1,2,3,4,5,6,7,8";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
