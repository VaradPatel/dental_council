package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestParamsTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationResponseTo;

import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.List;


/**
 * An interface for the customized repository of Track Application Details repository
 */
public interface IFetchTrackApplicationDetailsCustomRepository {
    /**
     * Retrieves the details of health professional applications requests based on the provided parameters.
     *
     * @param healthProfessionalApplicationRequestParamsTo - object containing the filter criteria for fetching application details
     * @param pagination                                   - object for pagination
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    HealthProfessionalApplicationResponseTo fetchTrackApplicationDetails(HealthProfessionalApplicationRequestParamsTo healthProfessionalApplicationRequestParamsTo, Pageable pagination, List<BigInteger> hpProfiles);
}
