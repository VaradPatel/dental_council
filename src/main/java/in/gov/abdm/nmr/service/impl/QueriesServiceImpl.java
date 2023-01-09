package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.Queries;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.QueriesDtoMapper;
import in.gov.abdm.nmr.repository.QueriesRepository;
import in.gov.abdm.nmr.service.IQueriesService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
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
    @Autowired
    private IWorkFlowService workFlowService;

    /**
     * Creates new queries in table
     *
     * @param queryCreateTo coming from controller
     * @return created list as it is
     */
    @Override
    public ResponseMessageTo createQueries(QueryCreateTo queryCreateTo) throws WorkFlowException {

        List<Queries> queries= new ArrayList<>();
        queryCreateTo.getQueries().forEach(queryTo -> {
            Queries query = Queries.builder()
                    .hpProfileId(queryCreateTo.getHpProfileId())
                    .commonComment(queryCreateTo.getCommonComment())
                    .queryBy(queryCreateTo.getQueryBy())
                    .fieldName(queryTo.getFieldName())
                    .fieldLabel(queryTo.getFieldLabel())
                    .sectionName(queryTo.getSectionName())
                    .queryComment(queryTo.getQueryComment())
                    .queryStatus(NMRConstants.OPEN_STATUS).build();
            queries.add(query);
        });

        queriesRepository.saveAll(queries);

        WorkFlowRequestTO workFlowRequestTO = WorkFlowRequestTO.builder().requestId(queryCreateTo.getRequestId())
                .applicationTypeId(ApplicationType.HP_REGISTRATION.getId())
                .hpProfileId(queryCreateTo.getHpProfileId())
                .actionId(Action.QUERY_RAISE.getId())
                .actorId(queryCreateTo.getGroupId())
                .build();
        workFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);
        return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
    }

    /**
     * get queries by hpProfileId
     *
     * @param hpProfileId profile id
     * @return List of queries object
     */
    @Override
    public List<QueryResponseTo> getQueriesByHpProfileId(BigInteger hpProfileId) {
        return queriesDtoMapper.queryDataToOpenQueriesDto(queriesRepository.findQueriesByHpProfileId(hpProfileId));
    }

    /**
     * Update query status
     *
     * @param queryIds list of query ids
     * @return string of message
     */
    @Override
    public ResponseMessageTo markQueryAsClosed(List<BigInteger> queryIds) {
        queryIds.stream().forEach(id -> {
            Queries queries = queriesRepository.findQueriesById(id);
            queries.setQueryStatus(NMRConstants.CLOSED_STATUS);
            queriesRepository.save(queries);
        });
        return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
    }
}

