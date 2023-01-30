package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class CollegeRegistrationRequestParamsTO {
    private Integer pageNo;
    private Integer limit;
    private String search;
    private String collegeId;
    private String collegeName;
    private String councilName;
    private String columnToSort;
    private String sortOrder;
}
