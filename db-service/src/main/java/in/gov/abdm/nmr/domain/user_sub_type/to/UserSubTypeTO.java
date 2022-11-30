package in.gov.abdm.nmr.domain.user_sub_type.to;

import in.gov.abdm.nmr.domain.user_type.to.UserTypeTO;
import lombok.Data;

@Data
public class UserSubTypeTO {

    private String code;
    private String name;
    private UserTypeTO userType;
}
