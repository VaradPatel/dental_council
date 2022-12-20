package in.gov.abdm.nmr.db.sql.domain.qualification_detail;

import java.math.BigInteger;

import in.gov.abdm.nmr.db.sql.domain.college.CollegeTO;
import in.gov.abdm.nmr.db.sql.domain.country.CountryTO;
import in.gov.abdm.nmr.db.sql.domain.course.CourseTO;
import in.gov.abdm.nmr.db.sql.domain.qualification_status.QualificationStatusTO;
import in.gov.abdm.nmr.db.sql.domain.state.StateTO;
import in.gov.abdm.nmr.db.sql.domain.university.UniversityTO;
import lombok.Data;

@Data
public class QualificationDetailRequestTO {
	private BigInteger id;
    private CountryTO country;
    private QualificationStatusTO qualificationStatus;
    private StateTO state;
    private CollegeTO college;
    private UniversityTO university;
    private CourseTO course;
    private String qualificationYear;
    private String qualificationMonth;
    private Integer is_name_change;
}
