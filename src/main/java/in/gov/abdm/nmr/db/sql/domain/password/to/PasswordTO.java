package in.gov.abdm.nmr.db.sql.domain.password.to;

import in.gov.abdm.nmr.db.sql.domain.user.to.UserTO;
import lombok.Data;

@Data
public class PasswordTO {

    private int id;
    private String value;
    private UserTO userDetail;
}
