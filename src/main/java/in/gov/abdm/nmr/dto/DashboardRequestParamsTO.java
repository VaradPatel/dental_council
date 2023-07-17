package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class DashboardRequestParamsTO {
    private String workFlowStatusId;
    private String applicationTypeId;
    private String userGroupStatus;
    private BigInteger userGroupId;
    private String smcId;
    private String councilName;
    private String registrationNumber;
    private String search;
    private String councilId;
    private String collegeId;
    private String applicantFullName;
    private String gender;
    private String emailId;
    private String mobileNumber;
    private String yearOfRegistration;
    private String sortBy;
    private String sortOrder;
    private String requestId;
}
