package in.gov.abdm.nmr.domain.user_sub_type;

import in.gov.abdm.nmr.domain.user_type.UserTypeTO;
import lombok.Data;

@Data
public class UserSubTypeTO {

    private Long id;
    private String name;
    private UserTypeTO userType;
}
