package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CollegeDeanCreationRequestTo {
    private BigInteger id;
    private String name;
    private String phoneNumber;
    private String emailId;
    private BigInteger userId;
    private String password;
}
