package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.QueryCreateTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import java.util.List;

/**
 * Interface to declare queries methods
 */
public interface IQueriesService {

    /**
     * Creates query
     */
    ResponseMessageTo createQueries(List<QueryCreateTo> queries);

    /**
     * Get queries by hpProfileId
     */
    List<QueryCreateTo> getQueriesByHpProfileId(Long hpProfileId);

    /**
     *Update status of raised query
     */
    ResponseMessageTo markQueryAsClosed(List<Long> queryIdList);
}
