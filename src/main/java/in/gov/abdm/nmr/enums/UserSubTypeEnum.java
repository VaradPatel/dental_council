package in.gov.abdm.nmr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
@Getter
public enum UserSubTypeEnum {

    COLLEGE(BigInteger.valueOf(1), "College", "College"), COLLEGE_REGISTRAR(BigInteger.valueOf(2), "College Registrar", "College"), COLLEGE_DEAN(BigInteger.valueOf(3), "College Dean", "College");

    private BigInteger code;
    private String name;
    private String userType;
}
