package in.gov.abdm.nmr.domain.user_detail.to;

import in.gov.abdm.nmr.domain.user_sub_type.to.UserSubTypeTO;
import in.gov.abdm.nmr.domain.user_type.to.UserTypeTO;
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
