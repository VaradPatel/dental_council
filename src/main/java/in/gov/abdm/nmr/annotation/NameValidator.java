package in.gov.abdm.nmr.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<Name, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null &&
                !s.equalsIgnoreCase("null") &&
                !s.isBlank() &&
                s.matches("^[a-zA-Z]+(?:[\s.]+[a-zA-Z]+)*$");
    }
}