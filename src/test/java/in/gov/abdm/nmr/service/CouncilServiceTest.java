package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.nosql.repository.ICouncilRepository;
import in.gov.abdm.nmr.service.impl.CouncilServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.REGISTRATION_NUMBER;
import static in.gov.abdm.nmr.util.CommonTestData.STATE_MEDICAL_COUNCIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouncilServiceTest {
    @Mock
    ICouncilRepository councilRepository;

    @InjectMocks
    CouncilServiceImpl councilService;

    @Test
    void testGetCouncilByRegistrationNumberAndCouncilNameReturnsCouncilDetails(){
        when(councilRepository.findCouncilByRegistrationNumberAndCouncilName(anyString(),anyString())).thenReturn(List.of(CommonTestData.getImrCouncilDetails()));
        List<Council> hpCouncilData = councilService.getCouncilByRegistrationNumberAndCouncilName(REGISTRATION_NUMBER, STATE_MEDICAL_COUNCIL);
        assertEquals(1, hpCouncilData.size());
    }
}
