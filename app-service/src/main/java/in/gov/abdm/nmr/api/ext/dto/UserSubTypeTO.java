package in.gov.abdm.nmr.api.ext.dto;

import lombok.Data;

@Data
public class UserSubTypeTO {

    private Long id;
    private String name;
    private UserTypeTO userType;
}
