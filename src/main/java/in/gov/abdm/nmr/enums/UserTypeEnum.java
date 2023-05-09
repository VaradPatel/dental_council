package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import in.gov.abdm.nmr.security.common.RoleConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTypeEnum {

    HEALTH_PROFESSIONAL(BigInteger.valueOf(1), RoleConstants.HEALTH_PROFESSIONAL, Group.HEALTH_PROFESSIONAL), //
    COLLEGE(BigInteger.valueOf(2), RoleConstants.COLLEGE, Group.COLLEGE), //
    SMC(BigInteger.valueOf(3), RoleConstants.STATE_MEDICAL_COUNCIL, Group.SMC), //
    NMC(BigInteger.valueOf(4), RoleConstants.NATIONAL_MEDICAL_COUNCIL, Group.NMC), //
    NBE(BigInteger.valueOf(5), RoleConstants.NATIONAL_BOARD_OF_EXAMINATIONS, Group.NBE), //
    SYSTEM(BigInteger.valueOf(6), RoleConstants.SYSTEM, Group.SYSTEM); //

    private BigInteger id;

    private String name;

    private Group group;

    public static UserTypeEnum getUserSubType(BigInteger userTypeId) {
        Optional<UserTypeEnum> optionalUserTypeEnum = Arrays.stream(UserTypeEnum.values()).filter(userType -> userType.getId().equals(userTypeId)).findFirst();
        return optionalUserTypeEnum.isPresent() ? optionalUserTypeEnum.get() : null;
    }
}
