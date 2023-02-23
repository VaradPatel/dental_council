package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class LoginResponseTO {

    private BigInteger profileId;
    private BigInteger userType;
    private BigInteger userSubType;
    private BigInteger userGroupId;
    private Boolean hpRegistered;
    private Boolean blacklisted;
}
