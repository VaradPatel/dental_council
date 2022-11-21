package in.gov.abdm.nmr.api.ext.user_detail;

import lombok.Data;

@Data
public class UserSubTypeTO {

    private Long id;
    private String name;
    private UserTypeTO userType;
}
