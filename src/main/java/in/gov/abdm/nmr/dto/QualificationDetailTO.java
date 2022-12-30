package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.dto.college.CollegeTO;
import lombok.Data;

@Data
public class QualificationDetailTO {
	private Integer id;
    private CountryTO country;
    private StateTO state;
    private CollegeTO college;
    private UniversityTO university;
    private CourseTO course;
    private String qualificationYear;
    private String qualificationMonth;
    private Integer isNameChange;
    private Integer isVerified;
//    private QualificationStatusTO qualificationStatus;
}
