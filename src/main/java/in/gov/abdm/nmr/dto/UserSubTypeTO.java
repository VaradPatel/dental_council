package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class UserSubTypeTO {

    private String id;
    private String name;
    private UserTypeTO userType;
}
