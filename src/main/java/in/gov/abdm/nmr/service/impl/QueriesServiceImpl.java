package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.QueryCreateTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.entity.Queries;
import in.gov.abdm.nmr.mapper.QueriesDtoMapper;
import in.gov.abdm.nmr.repository.QueriesRepository;
import in.gov.abdm.nmr.service.IQueriesService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementations of methods to handle queries raised by smc nmc and colleges
 */
@Service
@Transactional
public class QueriesServiceImpl implements IQueriesService {

    @Autowired
    QueriesRepository queriesRepository;

    @Autowired
    private QueriesDtoMapper queriesDtoMapper;

    /**
     * Creates new queries in table
     *
     * @param queryCreateTos coming from controller
     * @return created list as it is
     */
    @Override
    public ResponseMessageTo createQueries(List<QueryCreateTo> queryCreateTos) {
        queriesRepository.saveAll(queriesDtoMapper.queryDtoToData(queryCreateTos));
        return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
    }

    /**
     * get queries by hpProfileId
     *
     * @param hpProfileId profile id
     * @return List of queries object
     */
    @Override
    public List<QueryCreateTo> getQueriesByHpProfileId(Long hpProfileId) {
        return queriesDtoMapper.queryDataToDto(queriesRepository.findQueriesByHpProfileId(hpProfileId));
    }

    /**
     * Update query status
     *
     * @param queryIds list of query ids
     * @return string of message
     */
    @Override
    public ResponseMessageTo markQueryAsClosed(List<Long> queryIds) {
        queryIds.stream().forEach(id -> {
            Queries queries=queriesRepository.findQueriesById(id);
            queries.setQueryStatus(NMRConstants.CLOSED_STATUS);
            queriesRepository.save(queries);
        });
        return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
    }
}

