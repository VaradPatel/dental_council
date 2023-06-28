package in.gov.abdm.nmr.annotation;

import in.gov.abdm.nmr.enums.Action;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigInteger;

public class ActionIdValidator implements ConstraintValidator<ActionId, BigInteger> {
    @Override
    public boolean isValid(BigInteger s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && Action.getAction(s) != null;
    }
}
