package in.gov.abdm.nmr.db.sql.domain.user.to;

import java.math.BigInteger;

import in.gov.abdm.nmr.db.sql.domain.user_sub_type.to.UserSubTypeTO;
import in.gov.abdm.nmr.db.sql.domain.user_type.to.UserTypeTO;
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
