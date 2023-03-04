package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LoginTypeEnum {

    USERNAME_PASSWORD(BigInteger.valueOf(1)), MOBILE_OTP(BigInteger.valueOf(2)), NMR_ID_OTP(BigInteger.valueOf(3));

    private BigInteger code;
}
