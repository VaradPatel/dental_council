package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class DashboardTO {
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
    private BigInteger workFlowStatusId;
    private int pendency;
    private String gender;
    private String emailId;
    private String mobileNumber;
    private String nmrId;
    private String yearOfRegistration;
    private String collegeStatus;
    private BigInteger applicationTypeId;
    private Timestamp startDate;
    private Timestamp endDate;
    private String remark;
}
