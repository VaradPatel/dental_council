package in.gov.abdm.nmr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum UserTypeEnum {

    HEALTH_PROFESSIONAL(BigInteger.valueOf(1), "Health Professional", Group.HEALTH_PROFESSIONAL),
    COLLEGE(BigInteger.valueOf(2), "College", Group.COLLEGE),
    STATE_MEDICAL_COUNCIL(BigInteger.valueOf(3), "State Medical Council", Group.SMC),
    NATIONAL_MEDICAL_COUNCIL(BigInteger.valueOf(4), "National Medical Council", Group.NMC),
    NBE(BigInteger.valueOf(5), "NBE",Group.NBE),
    SYSTEM(BigInteger.valueOf(6), "SYSTEM", Group.SYSTEM);

    private BigInteger code;

    private String name;

    private Group group;


    public static UserTypeEnum getUserSubType(BigInteger userTypeId) {
        return Arrays.stream(UserTypeEnum.values()).filter(userType -> userType.getCode().equals(userTypeId)).findFirst().get();
    }

    public static UserTypeEnum getUserSubType(String name) {
        return Arrays.stream(UserTypeEnum.values()).filter(userType -> userType.getName().equals(name)).findFirst().get();
    }
}
