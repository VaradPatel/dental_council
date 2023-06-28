package in.gov.abdm.nmr.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static in.gov.abdm.nmr.util.NMRConstants.REGEX_FOR_REGISTRATION_NUMBER;

public class RegistrationNumberValidator implements ConstraintValidator<RegistrationNumber, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null &&
                !s.equalsIgnoreCase("null") &&
                !s.isBlank() &&
                s.matches(REGEX_FOR_REGISTRATION_NUMBER);
    }
}
