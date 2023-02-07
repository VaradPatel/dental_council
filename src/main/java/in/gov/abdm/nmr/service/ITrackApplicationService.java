package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationResponseTo;

/**
 * Service Layer to implement the Health Professional Track Applications Business Logic
 */
public interface ITrackApplicationService {

   /**
    * Retrieves information about the status of a health professional's requests for NMC, NBE, SMC, Dean, Registrar and Admin.
    *
    * @param healthProfessionalApplicationRequestTo - HealthProfessionalApplicationRequestTo object representing the request
    * @return the HealthProfessionalApplicationResponseTo object representing the response object
    * which contains all the details used to track the health professionals who have
    * raised a request
    */
   HealthProfessionalApplicationResponseTo fetchApplicationDetails(HealthProfessionalApplicationRequestTo healthProfessionalApplicationRequestTo);

   /**
    * Retrieves information about a health professional's application requests to track by health professional.
    *
    * @param healthProfessionalApplicationRequestTo - HealthProfessionalApplicationRequestTo object representing the request
    * @return the HealthProfessionalApplicationResponseTo object representing the response object
    * which contains all the details used to track the health professionals who have
    * raised a request
    */
   HealthProfessionalApplicationResponseTo fetchApplicationDetailsForHealthProfessional(HealthProfessionalApplicationRequestTo healthProfessionalApplicationRequestTo);
}
