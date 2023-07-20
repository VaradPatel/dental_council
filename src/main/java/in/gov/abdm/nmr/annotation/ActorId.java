package in.gov.abdm.nmr.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ActorIdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActorId {
    String message() default "Invalid actor ID. The supported actor are 1, 2, 3, 4, 5, 6, 7, 8.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
