package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PinCodeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PinCode {
    String message() default "Invalid pincode. it must contain only numbers and have a maximum length of 6.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
