package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

@Data
public class PractitionerQualififcationTO {
    @JsonProperty("nameOfDegreeOrDiplomaObtained")
    private BigInteger nameOfDegreeOrDiplomaObtained;
    @JsonProperty("country")
    private BigInteger country;
    @JsonProperty("state")
    private BigInteger state;
    @JsonProperty("college")
    private BigInteger college;
    @JsonProperty("university")
    private BigInteger university;
    @JsonProperty("monthOfAwardingDegreeDiploma")
    private String monthOfAwardingDegreeDiploma;
    @JsonProperty("yearOfAwardingDegreeDiploma")
    private String yearOfAwardingDegreeDiploma;
    @JsonProperty("degreeCertificate")
    private CertificateTO degreeCertificate;
    @JsonProperty("isNameDifferentInCertificate")
    private BigInteger isNameDifferentInCertificate;
}
