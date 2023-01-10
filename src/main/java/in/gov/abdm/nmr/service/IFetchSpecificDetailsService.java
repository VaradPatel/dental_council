package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.FetchSpecificDetailsResponseTO;
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
}
