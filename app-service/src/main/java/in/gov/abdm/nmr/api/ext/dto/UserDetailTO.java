package in.gov.abdm.nmr.api.ext.dto;

import lombok.Data;

@Data
public class UserDetailTO {

    private Long id;
    private String username;
    private String password;
    private String refreshTokenId;
    private UserTypeTO userType;
    private UserSubTypeTO userSubType;
}
