package in.gov.abdm.nmr.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**
 * The annotated element must not be {@code null} or a blank string.
 *
 * <p>This annotation can be applied to fields and methods. It will be evaluated at runtime.
 * The validation is performed by the {@link NotNullBlankValidator} class.
 *
 * <p>The {@code message} attribute allows to specify a custom error message to be returned in case
 * the validation fails. The message can contain placeholders in the form of {0}, {1}, etc.,
 * <p>
 * which will be replaced with the values of the annotated element and its constraints.
 *
 * <p>The {@code groups} and {@code payload} attributes allow to specify the validation groups
 * and payload, respectively.
 */
@Documented
@Constraint(validatedBy = NotNullBlankValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullBlank {

    /**
     * The error message to be returned in case the validation fails.
     *
     * @return the error message string
     */
    String message() default "the {0} should not be null or blank.";

    /**
     * The validation groups to which this constraint belongs.
     *
     * @return an array of validation group classes
     */
    Class<?>[] groups() default {};

    /**
     * The payload associated with the constraint.
     *
     * @return an array of payload classes
     */
    Class<? extends Payload>[] payload() default {};
}
