package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class HpProfileRegistrationResponseTO {
    private RegistrationDetailTO registrationDetailTO;
    private List<QualificationDetailResponseTo> qualificationDetailResponseTos;
    private NbeResponseTo nbeResponseTo;
    private BigInteger hpProfileId;
    private String requestId;
}
