package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.QueryCreateTo;
import in.gov.abdm.nmr.dto.QueryUpdateStatusTo;

import java.math.BigInteger;
import java.util.List;

/**
 * Interface to declare queries methods
 */
public interface IQueriesService {

    /**
     * Creates query
     */
    String createQueries(List<QueryCreateTo> queries);

    /**
     * Get queries by hpProfileId
     */
    List<QueryCreateTo> getQueriesByHpProfileId(Long hpProfileId);

    /**
     *Update status of raised query
     */
    String markQueryAsClosed(List<Long> queryIdList);
}
