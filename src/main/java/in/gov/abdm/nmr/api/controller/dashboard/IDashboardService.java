package in.gov.abdm.nmr.api.controller.dashboard;

import in.gov.abdm.nmr.api.controller.dashboard.to.ResponseTO;
import in.gov.abdm.nmr.api.exception.InvalidRequestException;

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
