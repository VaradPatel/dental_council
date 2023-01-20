package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class SMCProfileTO {
    private BigInteger id;
    private BigInteger userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private StateMedicalCouncilTO stateMedicalCouncil;
    private Integer ndhmEnrollment;
    private Integer enrolledNumber;
    private String displayName;
}
