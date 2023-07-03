package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AddressTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressType {

    Class<?>[] groups() default {};

    String message() default "Invalid address type. supportive address type are- business, current, permanent, communication, KYC";

    Class<? extends Payload>[] payload() default {};
}