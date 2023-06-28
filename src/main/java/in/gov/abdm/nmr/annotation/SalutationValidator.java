package in.gov.abdm.nmr.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static in.gov.abdm.nmr.util.NMRConstants.SALUTATION_DR;

public class SalutationValidator implements ConstraintValidator<Salutation, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null &&
                s.equalsIgnoreCase(SALUTATION_DR);
    }
}
