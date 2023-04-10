package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class QualificationDetailResponseTo {
    private BigInteger id;
    private CountryTO country;
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
    private String degreeCertificate;
    private String fileName;
    private String fileType;
    @JsonIgnore
    private Timestamp createdAt;
}
