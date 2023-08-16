package in.gov.abdm.nmr.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static in.gov.abdm.nmr.util.NMRConstants.REGEX_FOR_ALPHANUMERIC;

public class AlphanumericValidator implements ConstraintValidator<Alphanumeric, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.equals(""))
            return true;
        if (s.isBlank())
            return false;
        return s.matches(REGEX_FOR_ALPHANUMERIC);
    }
}

