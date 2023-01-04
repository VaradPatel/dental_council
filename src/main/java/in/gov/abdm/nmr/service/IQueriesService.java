package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.QueryCreateTo;
import java.util.List;

/**
 * Interface to declare queries methods
 */
public interface IQueriesService {

    /**
     * Creates query
     */
    List<QueryCreateTo> createQueries(List<QueryCreateTo> queries);

    /**
     * Get queries by hpProfileId
     */
    List<QueryCreateTo> getQueriesByHpProfileId(Long hpProfileId);

}
