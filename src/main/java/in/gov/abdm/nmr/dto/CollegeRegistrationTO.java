package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;


@Data
public class CollegeRegistrationTO {
    private BigInteger id;
    private String collegeId;
    private String collegeName;
    private String councilName;
    private String status;
    private Date submittedOn;
    private  Double pendency;

}
