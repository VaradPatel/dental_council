package in.gov.abdm.nmr.dto;

import lombok.Data;
/**
 *  A class with the attributes that helps to deal with search, sort and pagination
 * */
@Data
public class ReactivateHealthProfessionalRequestParam {
    private String applicantFullName;
    private String registrationNumber;
    private String gender;
    private String emailId;
    private String mobileNumber;
    private String yearOfRegistration;
    private String search;
    private Integer pageNo;
    private Integer offset;
    private String sortBy;
    private String sortType;
    private String groupId;
}
