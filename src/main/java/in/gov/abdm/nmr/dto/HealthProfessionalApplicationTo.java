package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class HealthProfessionalApplicationTo {
    private String doctorStatus;
    private String smcStatus;
    private String collegeDeanStatus;
    private String CollegeRegistrarStatus;
    private String nmcStatus;
    private String nbeStatus;
    private BigInteger hpProfileId;
    private String requestId;
    private String registrationNo;
    private String createdAt;
    private String councilName;
    private String applicantFullName;
    private BigInteger applicationTypeId;
}