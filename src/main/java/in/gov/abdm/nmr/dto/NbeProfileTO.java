package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.Email;
import in.gov.abdm.nmr.annotation.Name;
import in.gov.abdm.nmr.annotation.OptionalName;
import in.gov.abdm.validator.MobileNumber;
import lombok.Data;

import java.math.BigInteger;

@Data
public class NbeProfileTO {
    private BigInteger id;
    private BigInteger userId;
    @OptionalName
    private String firstName;
    @OptionalName
    private String lastName;
    @OptionalName
    private String middleName;
    @Name
    private String displayName;
    @Email
    private String emailId;
    @MobileNumber
    private String mobileNo;
}
