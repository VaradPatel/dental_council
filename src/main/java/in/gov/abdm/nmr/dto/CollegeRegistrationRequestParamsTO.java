package in.gov.abdm.nmr.dto;

import lombok.Data;
/**
 *  A class with the attributes that helps to deal with search, sort and pagination
 * */
@Data
public class CollegeRegistrationRequestParamsTO {
    private Integer pageNo;
    private Integer offset;
    private String search;
    private String collegeId;
    private String collegeName;
    private String councilName;
    private String sortBy;
    private String sortType;
}
