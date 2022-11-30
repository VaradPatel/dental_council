package in.gov.abdm.nmr.domain.password.to;

import in.gov.abdm.nmr.domain.user_detail.to.UserDetailTO;
import lombok.Data;

@Data
public class PasswordTO {

    private int id;
    private String value;
    private UserDetailTO userDetail;
}
