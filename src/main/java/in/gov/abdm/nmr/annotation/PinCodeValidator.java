package in.gov.abdm.nmr.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PinCodeValidator implements ConstraintValidator<PinCode, String> {
    @Override
    public boolean isValid(String pinCode, ConstraintValidatorContext constraintValidatorContext) {
        if (pinCode == null)
            return true;
        return pinCode.toString().matches("^[1-9]\\d{5}$");
    }
}
