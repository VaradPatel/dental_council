package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class HealthProfessionalApplicationTo {
    private String doctorStatus;
    private String smcStatus;
    private String collegeDeanStatus;
    private String collegeRegistrarStatus;
    private String nmcStatus;
    private String nbeStatus;
    private BigInteger hpProfileId;
    private String requestId;
    private String registrationNo;
    private String createdAt;
    private String councilName;
    private String applicantFullName;
    private BigInteger applicationTypeId;
    private String applicationTypeName;
    private Double pendency;
    private BigInteger workFlowStatusId;
    private String doctorActionDate;
    private String smcActionDate;
    private String collegeRegistrarActionDate;
    private String collegeDeanActionDate;
    private String nmcActionDate;
    private String nbeActionDate;
    private String gender;
    private String emailId;
    private String mobileNumber;
    private String nmrId;
}
