package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OptionalAddressValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalAddress {

    String message() default "Invalid address. it must be alphanumeric with allowed special characters (.,-:/()).";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
