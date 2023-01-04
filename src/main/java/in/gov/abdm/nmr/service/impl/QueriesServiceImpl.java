package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.QueryCreateTo;
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
    public String createQueries(List<QueryCreateTo> queryCreateTos) {
        queriesRepository.saveAll(queriesDtoMapper.queryDtoToData(queryCreateTos));
        return NMRConstants.DATA_INSERTED;
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
}

