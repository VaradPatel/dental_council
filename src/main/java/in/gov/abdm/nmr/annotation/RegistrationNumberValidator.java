package in.gov.abdm.nmr.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegistrationNumberValidator implements ConstraintValidator<RegistrationNumber, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null &&
                !s.equalsIgnoreCase("null") &&
                !s.isBlank() &&
                s.matches("[A-Z a-z 0-9]+[A-Z a-z 0-9//s //_//-]*[A-Z a-z 0-9]$");
    }
}
