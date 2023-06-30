package in.gov.abdm.nmr.annotation;

import in.gov.abdm.nmr.enums.Group;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigInteger;

public class ActorIdValidator implements ConstraintValidator<ActorId, BigInteger> {
    @Override
    public boolean isValid(BigInteger s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && Group.getGroup(s) != null;
    }
}
