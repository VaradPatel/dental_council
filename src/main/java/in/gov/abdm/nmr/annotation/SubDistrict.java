package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static in.gov.abdm.nmr.util.NMRConstants.SUB_DISTRICT_ERROR_MSG;

@Documented
@Constraint(validatedBy = SubDistrictValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SubDistrict {

    String message() default SUB_DISTRICT_ERROR_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
