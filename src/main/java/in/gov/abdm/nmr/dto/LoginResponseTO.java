package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class LoginResponseTO {

    private BigInteger profileId;
    private BigInteger collegeId;
    private BigInteger userId;
    private BigInteger userType;
    private BigInteger userSubType;
    private BigInteger userGroupId;
    private Boolean hpRegistered;
    private Boolean blacklisted;
    private String esignStatus;
}
