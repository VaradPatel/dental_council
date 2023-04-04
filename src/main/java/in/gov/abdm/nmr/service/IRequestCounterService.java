package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.jpa.entity.RequestCounter;
import in.gov.abdm.nmr.exception.WorkFlowException;

import java.math.BigInteger;

/**
 * Interface for generating and retrieving the request id.
 */
public interface IRequestCounterService {

    /**
     * Increments the counter and return the incremented count for a given application Type.
     * @param applicationTypeId the application type
     * @return the counter.
     */
    RequestCounter incrementAndRetrieveCount(BigInteger applicationTypeId) throws WorkFlowException;

}
