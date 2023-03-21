package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.NotNullBlank;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

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
    @NotNullBlank
    private CountryTO country;

    /**
     * State of the qualification request.
     */
    @NotNullBlank
    private StateTO state;

    /**
     * College of the qualification request.
     */
    @NotNullBlank
    private CollegeTO college;

    /**
     * University of the qualification request.
     */
    @NotNullBlank
    private UniversityTO university;

    /**
     * Course of the qualification request.
     */
    @NotNullBlank
    private CourseTO course;

    /**
     * Year of the qualification request.
     */
    @NotNullBlank
    private String qualificationYear;

    /**
     * Month of the qualification request.
     */
    @NotNullBlank
    private String qualificationMonth;

    /**
     * Flag to indicate if the name has changed in the qualification request.
     */
    @NotNullBlank
    private Integer isNameChange;

    /**
     * Flag to indicate if the qualification has been verified in the qualification request.
     */
    @NotNullBlank
    private Integer isVerified;

    /**
     * Unique identifier of the qualification request.
     */
    @NotNullBlank
    private String requestId;

    /**
     * Source of the qualification request.
     */
    @NotNullBlank
    private String qualificationFrom;

    private BigInteger broadSpecialityId;

    private String superSpecialityName;
    private String fileName;
}