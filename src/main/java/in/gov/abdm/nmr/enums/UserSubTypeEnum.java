package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import in.gov.abdm.nmr.security.common.RoleConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserSubTypeEnum {

    COLLEGE_ADMIN(BigInteger.valueOf(1), RoleConstants.COLLEGE_ADMIN, UserTypeEnum.COLLEGE, Group.COLLEGE,"College Admin"),
    COLLEGE_REGISTRAR(BigInteger.valueOf(2), RoleConstants.COLLEGE_REGISTRAR, UserTypeEnum.COLLEGE, Group.COLLEGE,"College Registrar"),
    COLLEGE_DEAN(BigInteger.valueOf(3), RoleConstants.COLLEGE_DEAN, UserTypeEnum.COLLEGE, Group.COLLEGE,"College Dean"),
    COLLEGE_PRINCIPAL(BigInteger.valueOf(4), RoleConstants.COLLEGE_PRINCIPAL, UserTypeEnum.COLLEGE, Group.COLLEGE,"College Principle"),
    OTHERS(BigInteger.valueOf(5), RoleConstants.COLLEGE_OTHERS, UserTypeEnum.COLLEGE, Group.COLLEGE,"College Other"),
    NMC_ADMIN(BigInteger.valueOf(6), RoleConstants.NATIONAL_MEDICAL_COUNCIL_ADMIN, UserTypeEnum.NMC, Group.NMC,""),
    NMC_VERIFIER(BigInteger.valueOf(7), RoleConstants.NATIONAL_MEDICAL_COUNCIL_VERIFIER, UserTypeEnum.NMC, Group.NMC,""),
    NBE_ADMIN(BigInteger.valueOf(8), RoleConstants.NATIONAL_BOARD_OF_EXAMINATIONS_ADMIN, UserTypeEnum.NBE, Group.NBE,""),
    NBE_VERIFIER(BigInteger.valueOf(9), RoleConstants.NATIONAL_BOARD_OF_EXAMINATIONS_VERIFIER, UserTypeEnum.NBE, Group.NBE,""),
    SMC_ADMIN(BigInteger.valueOf(10), RoleConstants.STATE_MEDICAL_COUNCIL_ADMIN, UserTypeEnum.SMC, Group.SMC,""),
    SMC_VERIFIER(BigInteger.valueOf(11), RoleConstants.STATE_MEDICAL_COUNCIL_VERIFIER, UserTypeEnum.SMC, Group.SMC,"");

    private BigInteger id;
    private String name;
    private UserTypeEnum userType;
    private Group group;
    private String displayName;

    public static UserSubTypeEnum getUserSubType(BigInteger userSubTypeId) {
        Optional<UserSubTypeEnum> optionalUserSubTypeEnum = Arrays.stream(UserSubTypeEnum.values()).filter(userSubType -> userSubType.getId().equals(userSubTypeId)).findFirst();
        return optionalUserSubTypeEnum.isPresent() ? optionalUserSubTypeEnum.get() : null;
    }

    public static List<UserSubTypeEnum> getUserSubTypeDisplayName(List<BigInteger> userSubTypeIds) {
        return Arrays.stream(UserSubTypeEnum.values())
                .filter(userSubType -> userSubTypeIds.contains(userSubType.getId()))
                .collect(Collectors.toList());
    }
}
