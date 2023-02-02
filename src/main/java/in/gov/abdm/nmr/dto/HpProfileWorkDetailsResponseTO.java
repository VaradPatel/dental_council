package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class HpProfileWorkDetailsResponseTO {
    private SpecialityDetailsTO specialityDetails;
    private WorkDetailsTO workDetails;
    private CurrentWorkDetailsTO currentWorkDetails;
    private BigInteger hpProfileId;
    private String requestId;

}
