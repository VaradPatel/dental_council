package in.gov.abdm.nmr.annotation;

import in.gov.abdm.nmr.enums.ApplicationType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigInteger;

public class ApplicationTypeIdValidator implements ConstraintValidator<ApplicationTypeId, BigInteger> {
    @Override
    public boolean isValid(BigInteger s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && ApplicationType.getApplicationType(s) != null;
    }
}
