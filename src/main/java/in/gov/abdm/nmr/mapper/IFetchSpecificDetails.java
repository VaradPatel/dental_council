package in.gov.abdm.nmr.mapper;

import java.util.Date;

/**
 * Interface Based Projection - to fetch the partial result-set
 * Suggested Alternative to class based projections[DTOs]
 */
public interface IFetchSpecificDetails {

    /**
     * Abstract method to fetch the Registration number assigned to the application
     * @return name
     */
    String getRegistrationNo();

    /**
     * Abstract method to fetch the full name of the applicant
     * @return count
     */
    String getNameOfApplicant();

    /**
     * Abstract method to fetch the State Council Name
     * @return
     */
    String getNameOfStateCouncil();

    /**
     * Abstract method to fetch the registration date of the application
     */
    Date getDateOfSubmission();

    /**
     * Abstract method to fetch the type of the user who had verified the application
     */
    String getVerifiedByUserType();

    /**
     * Abstract method to fetch the subtype of the user who had verified the application
     */
    String getVerifiedByUserSubType();

    /**
     * Abstract method to fetch the current HP Profile Status of the application
     */
    String getHpProfileStatus();


}
