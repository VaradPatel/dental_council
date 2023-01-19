package in.gov.abdm.nmr.mapper;

import java.math.BigInteger;

/**
 * Interface Based Projection - to fetch the partial result-set
 * Suggested Alternative to class based projections[DTOs]
 */
public interface INextGroup {

    /**
     * Abstract method to fetch the current Group ID
     * @return current Group Id
     */
    BigInteger getAssignTo();

    /**
     * Abstract method to fetch the current Work-Flow status
     */
    BigInteger getWorkFlowStatusId();
}
