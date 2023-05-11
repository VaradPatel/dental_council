package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class UserRequestParamsTO {
    private String userTypeId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobileNumber;
    private String sortBy;
    private String sortOrder;
    private String userSubTypeID;
}
