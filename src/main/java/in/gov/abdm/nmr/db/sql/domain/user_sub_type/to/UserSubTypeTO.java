package in.gov.abdm.nmr.db.sql.domain.user_sub_type.to;

import in.gov.abdm.nmr.db.sql.domain.user_type.to.UserTypeTO;
import lombok.Data;

@Data
public class UserSubTypeTO {

    private String id;
    private String name;
    private UserTypeTO userType;
}
