package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;

import java.util.List;

/**
 * Service Layer to implement the actual Business Logic
 */
public interface IFetchSpecificDetailsService {
    /**
     * This method retrieves the details to be listed according to the card selected
     *
     * @return FetchSpecificDetailsResponseTO
     */
    List<FetchSpecificDetailsResponseTO> fetchSpecificDetails(String groupName, String applicationType, String workFlowStatus) throws InvalidRequestException;

    /**
     * This method fetches the dashboard data based on the input request.
     * @param dashboardRequestTO The request object containing the parameters for fetching dashboard data.
     * @return DashboardResponseTO The response object containing the details fetched from dashboard.
     * @throws InvalidRequestException If the input request is invalid.
     */
    DashboardResponseTO fetchDashboardData(DashboardRequestTO dashboardRequestTO) throws InvalidRequestException;

}
