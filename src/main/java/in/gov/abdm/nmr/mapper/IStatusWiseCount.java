package in.gov.abdm.nmr.mapper;

import java.math.BigInteger;

/**
 * Interface Based Projection - to fetch the partial result-set
 * Suggested Alternative to class based projections[DTOs]
 */
public interface IStatusWiseCount {

    /**
     * Abstract method to fetch the application status name
     * @return name
     */
    String getName();

    /**
     * Abstract method to fetch the count of applications
     * according to their status at that point in time
     * @return count
     */
    BigInteger getCount();
}
