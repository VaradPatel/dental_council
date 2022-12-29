package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ResponseTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;

/**
 * Service Layer to implement the actual Business Logic
 */
public interface IDashboardService {

    /**
     * This method retrieves the Count of applications according to their status
     * @return ResponseTO
     */
     ResponseTO fetchCountOnCard(String userType, String userSubType) throws InvalidRequestException;
}
