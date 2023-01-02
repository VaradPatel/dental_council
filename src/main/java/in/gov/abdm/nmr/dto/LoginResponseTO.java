package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class LoginResponseTO {

    private BigInteger profileId;
    private BigInteger userType;
    private BigInteger userSubType;
    private Boolean hpRegistered;
    private Boolean blacklisted;
}