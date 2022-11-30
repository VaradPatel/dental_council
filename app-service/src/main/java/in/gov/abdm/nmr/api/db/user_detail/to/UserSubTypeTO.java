package in.gov.abdm.nmr.api.db.user_detail.to;

import lombok.Value;

@Value
public class UserSubTypeTO {

    private String code;
    private String name;
    private UserTypeTO userType;
}
