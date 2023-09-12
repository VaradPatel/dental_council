package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.NotNullBlank;
import in.gov.abdm.nmr.annotation.OptionalName;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * The QualificationDetailRequestTO class is used to store data related to a qualification detail request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualificationDetailRequestTO {

    /**
     * Unique identifier of the qualification request.
     */
    private BigInteger id;

    /**
     * Country of the qualification request.
     */
    @NotNullBlank(message = SELECT_COUNTRY)
    private CountryTO country;

    /**
     * State of the qualification request.
     */
    @NotNullBlank(message = SELECT_STATE)
    private StateTO state;

    /**
     * College of the qualification request.
     */
    @NotNullBlank(message = SELECT_COLLEGE)
    private CollegeTO college;

    /**
     * University of the qualification request.
     */
    @NotNullBlank(message = SELECT_UNIVERSITY)
    private UniversityTO university;

    /**
     * Course of the qualification request.
     */
    @NotNullBlank(message = SELECT_COURSE)
    private CourseTO course;

    /**
     * Year of the qualification request.
     */
    @NotNullBlank(message = SELECT_QUALIFICATION_YEAR)
    private String qualificationYear;

    /**
     * Month of the qualification request.
     */
    @NotNullBlank(message = SELECT_QUALIFICATION_MONTH)
    private String qualificationMonth;

    /**
     * Flag to indicate if the name has changed in the qualification request.
     */
    private Integer isNameChange;

    /**
     * Flag to indicate if the qualification has been verified in the qualification request.
     */
    private Integer isVerified;

    /**
     * Unique identifier of the qualification request.
     */
    private String requestId;

    /**
     * Source of the qualification request.
     */
    @NotNullBlank(message = SELECT_QUALIFICATION_FROM)
    private String qualificationFrom;

    private BigInteger broadSpecialityId;

    @OptionalName(message = "Please enter a valid Super Speciality")
    private String superSpecialityName;

    private String fileName;

}