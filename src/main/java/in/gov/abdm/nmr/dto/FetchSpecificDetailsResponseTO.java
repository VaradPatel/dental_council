package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchSpecificDetailsResponseTO {

    /**
     * Registration number assigned to the application
     */
    private String registrationNo;

    /**
     * Full name of the applicant
     */
    private String nameOfApplicant;

    /**
     * State Council Name
     */
    private String nameOfStateCouncil;

    /**
     * Current verification status of the application
     * with the State Medical Council
     */
    private String councilVerificationStatus;

    /**
     * Current verification status of the application
     * with the College
     */
    private String collegeVerificationStatus;

    /**
     * Current verification status of the application
     * with the National Medical Council
     */
    private String nMCVerificationStatus;

    /**
     * Registration Date of the application
     */
    private Date dateOfSubmission;

    /**
     * Delay in verification of application
     */
    private BigInteger pendency;
}
