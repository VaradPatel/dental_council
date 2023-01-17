package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Data;

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
    private String website;
    private String address;
    private String requestId;
    private String pinCode;
    private BigInteger stateId;
}
