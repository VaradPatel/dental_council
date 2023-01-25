package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class NbeProfileTO {
    private BigInteger id;
    private BigInteger userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String displayName;
    private String emailId;
    private String mobileNo;
}
