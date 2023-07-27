package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static in.gov.abdm.nmr.util.NMRConstants.PIN_CODE_ERROR_MSG;

@Documented
@Constraint(validatedBy = PinCodeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PinCode {
    String message() default PIN_CODE_ERROR_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
