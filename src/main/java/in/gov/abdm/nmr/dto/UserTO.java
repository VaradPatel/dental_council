package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import in.gov.abdm.nmr.dto.UserSubTypeTO;
import in.gov.abdm.nmr.dto.UserTypeTO;
import lombok.Data;

@Data
public class UserTO {

    private BigInteger id;
    private String username;
    private String password;
    private String refreshTokenId;
    private UserTypeTO userType;
    private UserSubTypeTO userSubType;
}
