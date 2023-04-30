package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;

import java.nio.file.AccessDeniedException;

/**
 * Service Layer to implement the actual Business Logic
 */
public interface IFetchCountOnCardService {

    /**
     * This method retrieves the Count of applications according to their status
     * @return ResponseTO
     */

    FetchCountOnCardResponseTO fetchCountOnCard() throws AccessDeniedException;
}
