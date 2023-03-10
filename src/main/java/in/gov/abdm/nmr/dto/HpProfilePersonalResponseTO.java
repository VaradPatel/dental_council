package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class HpProfilePersonalResponseTO {
    private PersonalDetailsTO personalDetails;
    private AddressTO communicationAddress;
    private AddressTO kycAddress;
    private BigInteger hpProfileId;
    private String requestId;
    private BigInteger applicationTypeId;
    private String nmrId;

}
