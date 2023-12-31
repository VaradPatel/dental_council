package in.gov.abdm.nmr.service;


import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.repository.IStateMedicalCouncilRepository;
import in.gov.abdm.nmr.service.impl.StateMedicalCouncilDaoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StateMedicalCouncilDaoServiceTest {

    @Mock
    IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    @InjectMocks
    StateMedicalCouncilDaoServiceImpl stateMedicalCouncilDaoService;

    @Test
    void testFindByStateShouldReturnCouncilAsPerState(){
        when(stateMedicalCouncilRepository.findByState(anyString())).thenReturn(getStateMedicalCouncil());
        StateMedicalCouncil stateMedicalCouncil = stateMedicalCouncilDaoService.findByState(STATE_NAME);
        assertEquals( STATE_NAME, stateMedicalCouncil.getState());
    }

    @Test
    void testGetAllStateMedicalCouncilShouldReturnAllCouncils(){
        when(stateMedicalCouncilRepository.findAll(any(Sort.class))).thenReturn(List.of(getStateMedicalCouncil()));
        List<StateMedicalCouncilTO> stateMedicalCouncils = stateMedicalCouncilDaoService.getAllStateMedicalCouncil();
        assertEquals(1, stateMedicalCouncils.size());
        assertEquals(STATE_MEDICAL_COUNCIL,stateMedicalCouncils.get(0).getName());

    }
}
