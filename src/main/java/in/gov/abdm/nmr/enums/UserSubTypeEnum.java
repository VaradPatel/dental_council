package in.gov.abdm.nmr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Arrays;

import static in.gov.abdm.nmr.util.NMRConstants.COLLEGE_CONSTANT;

@AllArgsConstructor
@Getter
public enum UserSubTypeEnum {

    COLLEGE(BigInteger.valueOf(1), COLLEGE_CONSTANT, UserTypeEnum.COLLEGE.getName(), Group.COLLEGE),
    COLLEGE_REGISTRAR(BigInteger.valueOf(2), "College Registrar", UserTypeEnum.COLLEGE.getName(), Group.COLLEGE),
    COLLEGE_DEAN(BigInteger.valueOf(3), "College Dean", UserTypeEnum.COLLEGE.getName(), Group.COLLEGE),
    COLLEGE_PRINCIPAL(BigInteger.valueOf(4), "College Principal", UserTypeEnum.COLLEGE.getName(), Group.COLLEGE),
    OTHERS(BigInteger.valueOf(5), "Others", UserTypeEnum.COLLEGE.getName(), Group.COLLEGE),
    NMC_ADMIN(BigInteger.valueOf(6), "NMC Admin", UserTypeEnum.NATIONAL_MEDICAL_COUNCIL.getName(), Group.NMC),
    NMC_VERIFIER(BigInteger.valueOf(7), "NMC Verifier", UserTypeEnum.NATIONAL_MEDICAL_COUNCIL.getName(), Group.NMC);

    private BigInteger code;
    private String name;
    private String userType;
    private Group group;

    public static UserSubTypeEnum getUserSubType(BigInteger userSubTypeId) {
        return Arrays.stream(UserSubTypeEnum.values()).filter(userSubType -> userSubType.getCode().equals(userSubTypeId)).findFirst().get();
    }
}
