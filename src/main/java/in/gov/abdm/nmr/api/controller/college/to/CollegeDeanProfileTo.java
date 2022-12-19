package in.gov.abdm.nmr.api.controller.college.to;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CollegeDeanProfileTo {
    private BigInteger id;
    private String name;
    private String phoneNumber;
    private String emailId;
    private BigInteger userId;
}
