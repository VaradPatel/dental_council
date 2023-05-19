package in.gov.abdm.nmr.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalRequestParam;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalResponseTO;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.repository.impl.ElasticsearchRepositoryImpl;
import in.gov.abdm.nmr.repository.impl.WorkFlowCustomRepositoryImpl;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkFlowCustomRepositoryTest {

    @InjectMocks
    WorkFlowCustomRepositoryImpl workFlowCustomRepository;
    @Mock
    EntityManager entityManager;

/*    @Test
    void testValidateUser() {
        when(workFlowCustomRepository.getReactivationRecordsOfHealthProfessionalsToNmc(
                any(ReactivateHealthProfessionalRequestParam.class), Pageable.ofSize(1)))
                .thenReturn(new ReactivateHealthProfessionalResponseTO());

        ReactivateHealthProfessionalResponseTO reactivationRecordsOfHealthProfessionalsToNmc = workFlowCustomRepository
                .getReactivationRecordsOfHealthProfessionalsToNmc(
                        any(ReactivateHealthProfessionalRequestParam.class), Pageable.ofSize(1));
        assertEquals(ID, reactivationRecordsOfHealthProfessionalsToNmc.getTotalNoOfRecords());
    }*/
}