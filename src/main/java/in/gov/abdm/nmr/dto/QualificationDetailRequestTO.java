package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import in.gov.abdm.nmr.dto.college.CollegeTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private String qualificationFrom;

}
