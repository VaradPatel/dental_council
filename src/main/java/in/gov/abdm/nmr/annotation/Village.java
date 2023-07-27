package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static in.gov.abdm.nmr.util.NMRConstants.VILLAGE_ERROR_MSG;

@Documented
@Constraint(validatedBy = VillageValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Village {

    String message() default VILLAGE_ERROR_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
