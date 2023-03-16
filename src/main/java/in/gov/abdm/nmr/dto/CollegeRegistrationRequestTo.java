package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CollegeRegistrationRequestTo {
    private BigInteger id;
    private String name;
    private String collegeCode;
    private String phoneNumber;
    private String emailId;
    private BigInteger userId;
    private BigInteger councilId;
    private BigInteger universityId;
    private BigInteger collegeId;
    private String website;
    private String address;
    private String requestId;
    private String pinCode;
    private BigInteger stateId;
    private boolean isApproved;
}
