package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class PractitionerRegistrationTO {
    @JsonProperty("registeredWithCouncil")
    private String registeredWithCouncil;
    @JsonProperty("registrationNumber")
    private String registrationNumber;
    @JsonProperty("registrationCertificate")
    private String registrationCertificate;
    @JsonProperty("categoryId")
    private BigInteger categoryId;
    @JsonProperty("isNameDifferentInCertificate")
    private BigInteger isNameDifferentInCertificate;
    @JsonProperty("qualifications")
    private List<PractitionerQualififcationTO> qualifications;

}
