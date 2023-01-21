package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import in.gov.abdm.nmr.dto.college.CollegeTO;
import lombok.Data;

@Data
public class QualificationDetailRequestTO {
	private BigInteger id;
    private CountryTO country;
//    private QualificationStatusTO qualificationStatus;
    private StateTO state;
    private CollegeTO college;
    private UniversityTO university;
    private CourseTO course;
    private String qualificationYear;
    private String qualificationMonth;
    private Integer isNameChange;
    private Integer isVerified;
    private String requestId;
    
    private String roll_no;
    private String result;
    private String year;
    private String month;
    private Integer marks_obtained;
    private String qualificationFrom;

}
