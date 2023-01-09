package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.OpenQueriesResponseTo;
import in.gov.abdm.nmr.dto.QueryCreateTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.exception.WorkFlowException;

import java.math.BigInteger;
import java.util.List;

/**
 * Interface to declare queries methods
 */
public interface IQueriesService {

    /**
     * Creates query
     */
    ResponseMessageTo createQueries(QueryCreateTo queryCreateTo) throws WorkFlowException;

    /**
     * Get queries by hpProfileId
     */
    List<OpenQueriesResponseTo> getQueriesByHpProfileId(BigInteger hpProfileId);

    /**
     *Update status of raised query
     */
    ResponseMessageTo markQueryAsClosed(List<BigInteger> queryIdList);
}
