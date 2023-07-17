package in.gov.abdm.nmr.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static in.gov.abdm.nmr.util.NMRConstants.REGEX_FOR_EMAIL;

public class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        return s != null &&
                !s.equalsIgnoreCase("null") &&
                !s.isBlank() &&
                s.matches(REGEX_FOR_EMAIL);
    }
}