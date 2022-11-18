package in.gov.abdm.nmr.domain.password;

import in.gov.abdm.nmr.domain.user_detail.UserDetailTO;
import lombok.Data;

@Data
public class PasswordTO {

    private int id;
    private String value;
    private UserDetailTO userDetail;
}
