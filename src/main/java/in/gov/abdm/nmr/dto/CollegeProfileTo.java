package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CollegeProfileTo {
    private BigInteger id;
    private String name;
    private String collegeCode;
    private String phoneNumber;
    private String emailId;
    private BigInteger userId;
    private String stateName;
    private String councilName;
    private String universityName;
    private String website;
    private String address;
    private String pinCode;
    private String requestId;
    private boolean isApproved;
}