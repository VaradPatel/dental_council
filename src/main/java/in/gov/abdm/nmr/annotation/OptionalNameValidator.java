package in.gov.abdm.nmr.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OptionalNameValidator implements ConstraintValidator<OptionalName, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.equals(""))
            return true;
        if (s.isBlank())
            return false;
        return s.matches("^[a-zA-Z]+(?:[\s.]+[a-zA-Z]+)*$");
    }
}