package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.DashboardResponseTO;
import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.dto.NMRPagination;
import in.gov.abdm.nmr.exception.InvalidRequestException;

import java.nio.file.AccessDeniedException;

/**
 * Service Layer to implement the actual Business Logic
 */
public interface IDashboardService {

    /**
     * This method retrieves the Count of applications according to their status
     *
     * @return ResponseTO
     */

    FetchCountOnCardResponseTO fetchCountOnCard() throws AccessDeniedException;

    DashboardResponseTO fetchCardDetails(String workFlowStatusId, String applicationTypeId, String userGroupStatus, String search, String value, NMRPagination nmrPagination) throws InvalidRequestException;
}

