package in.gov.abdm.nmr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.COLLEGE_CONSTANT;

@AllArgsConstructor
@Getter
public enum UserSubTypeEnum {

    COLLEGE(BigInteger.valueOf(1), COLLEGE_CONSTANT, COLLEGE_CONSTANT),
    COLLEGE_REGISTRAR(BigInteger.valueOf(2), "College Registrar", COLLEGE_CONSTANT),
    COLLEGE_DEAN(BigInteger.valueOf(3), "College Dean", COLLEGE_CONSTANT),
    NMC_ADMIN(BigInteger.valueOf(6), "NMC Admin", COLLEGE_CONSTANT),
    NMC_VERIFIER(BigInteger.valueOf(7), "NMC Verifier", COLLEGE_CONSTANT);

    private BigInteger code;
    private String name;
    private String userType;
}
