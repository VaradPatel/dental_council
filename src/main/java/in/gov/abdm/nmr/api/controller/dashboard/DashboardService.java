package in.gov.abdm.nmr.api.controller.dashboard;

import in.gov.abdm.nmr.api.controller.dashboard.to.ResponseTO;

/**
 * Service Layer to implement the actual Business Logic
 */
public interface DashboardService {

    /**
     * This method retrieves the Count of applications according to their status
     * @return ResponseTO
     */
    ResponseTO fetchCountOnCard();
}
