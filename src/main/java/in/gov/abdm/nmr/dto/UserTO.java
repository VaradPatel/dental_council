package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTO {

    private BigInteger id;
    private BigInteger userTypeId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobileNumber;

}
