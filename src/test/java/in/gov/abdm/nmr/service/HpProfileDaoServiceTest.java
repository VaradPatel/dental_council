package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.HpSmcDetailTO;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.NoDataFoundException;
import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.nosql.entity.RegistrationsDetails;
import in.gov.abdm.nmr.repository.IStateMedicalCouncilRepository;
import in.gov.abdm.nmr.service.impl.HpProfileDaoServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HpProfileDaoServiceTest {
    @InjectMocks
    HpProfileDaoServiceImpl hpProfileDaoService;

    @Mock
    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    @Mock
    private ICouncilService councilService;

    @Test
    void testFetchSmcRegistrationDetailShouldReturnMatchingHealthProfessionalDetails() throws NmrException, NoDataFoundException {
        when(stateMedicalCouncilRepository.findStateMedicalCouncilById(any(BigInteger.class))).thenReturn(CommonTestData.getStateMedicalCouncil());
        when(councilService.getCouncilByRegistrationNumberAndCouncilName(anyString(),anyString())).thenReturn(List.of(CommonTestData.getImrCouncilDetails()));

        HpSmcDetailTO hpSmcDetailTO = hpProfileDaoService.fetchSmcRegistrationDetail(1, REGISTRATION_NUMBER);
        assertEquals(PROFILE_DISPLAY_NAME, hpSmcDetailTO.getHpName());
        assertEquals(REGISTRATION_NUMBER, hpSmcDetailTO.getRegistrationNumber());
        assertEquals(STATE_MEDICAL_COUNCIL,hpSmcDetailTO.getCouncilName());

    }
}
