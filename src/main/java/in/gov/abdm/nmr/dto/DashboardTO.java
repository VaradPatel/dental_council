package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class DashboardTO {
    private String doctor;
    private String smc;
    private String CollegeDean;
    private String CollegeRegistrar;
    private String nmc;
    private BigInteger hpProfileId;
    private String requestId;
    private String registrationNo;
    private String createdAt;
    private String name;
    private String fullName;
}
