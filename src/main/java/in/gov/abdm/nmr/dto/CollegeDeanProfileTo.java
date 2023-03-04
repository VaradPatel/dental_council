package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CollegeDeanProfileTo {
    private BigInteger id;
    private String name;
    private String phoneNumber;
    private String emailId;
    private BigInteger userId;
}
