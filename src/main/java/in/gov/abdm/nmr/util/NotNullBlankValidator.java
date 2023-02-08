package in.gov.abdm.nmr.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The NotNullBlankValidator class is a constraint validator that implements the ConstraintValidator interface.
 * It checks if an object passed as an argument is not null and not an empty string.
 */
public class NotNullBlankValidator implements ConstraintValidator<NotNullBlank, Object> {

    /**
     * This method overrides the isValid method from the ConstraintValidator interface.
     * It checks if the object passed as an argument is not null and not an empty string.
     *
     * @param object                     the object to be validated
     * @param constraintValidatorContext the constraint validator context
     * @return true if the object is not null and not an empty string, false otherwise
     */
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        return object != null && object != "";
    }
}
