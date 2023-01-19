package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTypeEnum {

    HEALTH_PROFESSIONAL(BigInteger.valueOf(1), "Health Professional"), COLLEGE(BigInteger.valueOf(2), "College"), //
    STATE_MEDICAL_COUNCIL(BigInteger.valueOf(3), "State Medical Council"), NATIONAL_MEDICAL_COUNCIL(BigInteger.valueOf(4), "National Medical Council"), //
    NBE(BigInteger.valueOf(5), "NBE");

    private BigInteger code;

    private String name;
}
