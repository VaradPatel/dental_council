package in.gov.abdm.nmr.api.security.controller.to;

import java.math.BigInteger;

import lombok.Data;

@Data
public class LoginResponseTO {

    private BigInteger profileId;
    private Long userType;
    private Long userSubType;
    private Boolean hpRegistered;
    private Boolean blacklisted;
}
