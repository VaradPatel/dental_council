package in.gov.abdm.nmr.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressTypeValidator implements ConstraintValidator<AddressType, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null &&
                in.gov.abdm.nmr.enums.AddressType.getAddressType(s) != null;
    }
}