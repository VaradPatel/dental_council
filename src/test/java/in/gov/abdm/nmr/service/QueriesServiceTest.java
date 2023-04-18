package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.Queries;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.repository.QueriesRepository;
import in.gov.abdm.nmr.service.impl.QueriesServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueriesServiceTest {

    @Mock
    QueriesRepository queriesRepository;
    @Mock
    IWorkFlowService workFlowService;
    @InjectMocks
    QueriesServiceImpl queriesService;

    @Test
    void testCreateQueriesCreatesAndSavesQuery() throws WorkFlowException, InvalidRequestException {
        when(queriesRepository.saveAll(any())).thenReturn(Collections.emptyList());
        doNothing().when(workFlowService).initiateSubmissionWorkFlow(any(WorkFlowRequestTO.class));
        ResponseMessageTo queries = queriesService.createQueries(getQueries());
        assertEquals(NMRConstants.SUCCESS_RESPONSE, queries.getMessage());

    }

    @Test
    void testGetQueriesByHpProfileIdShouldReturnListOfOpenQueries(){
        when(queriesRepository.findOpenQueriesByHpProfileId(any())).thenReturn(List.of(getQueriesEntity()));
        List<QueryResponseTo> queryResponse = queriesService.getQueriesByHpProfileId(ID);
        assertTrue(queryResponse.size() == 1);
        QueryResponseTo queryResponseTo = queryResponse.get(0);
        assertEquals(QUERY_COMMENT, queryResponseTo.getCommonComment());
        assertEquals(QUERY_ON, queryResponseTo.getFieldLabel());

    }

    @Test
    void testMarkQueriesAsClosedUpdatesQueryStatus(){
        when(queriesRepository.saveAll(any())).thenReturn(Collections.emptyList());
        when(queriesRepository.findOpenQueriesByHpProfileId(any())).thenReturn(List.of(getQueriesEntity()));
        ResponseMessageTo responseMessageTo = queriesService.markQueryAsClosed(ID);
        assertEquals(NMRConstants.SUCCESS_RESPONSE, responseMessageTo.getMessage());


    }

    private Queries  getQueriesEntity() {
        Queries queries =  new Queries();
        queries.setQueryBy(Group.SMC.name());
        queries.setCommonComment(QUERY_COMMENT);
        queries.setHpProfileId(ID);
        queries.setQueryStatus(NMRConstants.QUERY_OPEN_STATUS);
        queries.setFieldLabel(QUERY_ON);
        queries.setFieldName(QUERY_ON);
        queries.setSectionName(QUERY_SECTION);
        queries.setId(ID);
        return queries;
    }

    private QueryCreateTo getQueries() {
        QueryCreateTo queryCreateTo = new QueryCreateTo();
        queryCreateTo.setQueryBy(Group.SMC.name());
        queryCreateTo.setCommonComment(QUERY_COMMENT);
        queryCreateTo.setGroupId(Group.SMC.getId());
        queryCreateTo.setApplicationTypeId(ApplicationType.HP_REGISTRATION.getId());
        queryCreateTo.setHpProfileId(ID);
        queryCreateTo.setRequestId(REQUEST_ID);
        QueryTo queryTo = new QueryTo();
        queryTo.setQueryComment(QUERY_COMMENT);
        queryTo.setQueryStatus(NMRConstants.QUERY_OPEN_STATUS);
        queryTo.setFieldLabel(QUERY_ON);
        queryTo.setFieldName(QUERY_ON);
        queryTo.setSectionName(QUERY_SECTION);
        queryCreateTo.setQueries(List.of(queryTo));
        return queryCreateTo;
    }
}
