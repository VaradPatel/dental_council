package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class HpProfilePersonalResponseTO {
    private PersonalDetailsTO personalDetails;
    private AddressTO communicationAddress;
    private IMRDetailsTO imrDetails;
    private BigInteger hpProfileId;

}
