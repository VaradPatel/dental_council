package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AddressValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Address {
    String message() default "Invalid Address; it must be alphanumeric with allowed special characters: " +
            "(.,-:/())";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
