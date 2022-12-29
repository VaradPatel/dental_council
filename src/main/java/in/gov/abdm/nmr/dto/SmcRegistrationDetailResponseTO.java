package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SmcRegistrationDetailResponseTO {

    private String hpName;
    private String registrationNumber;
    private String councilName;
    private BigInteger hpProfileId;
}
