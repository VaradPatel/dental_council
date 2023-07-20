package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ActionIdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionId {
    String message() default "Invalid action id. supported actions are 1, 2, 3, 4, 5, 6, 7.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
