package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static in.gov.abdm.nmr.util.NMRConstants.ADDRESS_TYPE_ERROR_MSG;

@Documented
@Constraint(validatedBy = AddressTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressType {

    Class<?>[] groups() default {};

    String message() default ADDRESS_TYPE_ERROR_MSG;

    Class<? extends Payload>[] payload() default {};
}