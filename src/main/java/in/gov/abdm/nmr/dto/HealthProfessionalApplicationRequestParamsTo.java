package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class HealthProfessionalApplicationRequestParamsTo {
    private String smcId;
    private String registrationNumber;
    private String workFlowStatusId;
    private String applicationTypeId;
    private String applicantFullName;
    private String gender;
    private String emailId;
    private String mobileNumber;
    private String yearOfRegistration;
    private int pageNo;
    private int offset;
    private String sortBy;
    private String sortOrder;
    private BigInteger hpProfileId;
    private String collegeId;
    private String requestId;
}