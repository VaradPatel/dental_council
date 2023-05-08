package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.RegistrationDetailsMaster;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.repository.RegistrationDetailMasterRepository;
import in.gov.abdm.nmr.service.impl.AccessControlServiceImpl;
import in.gov.abdm.nmr.service.impl.RegistrationDetailMasterDaoServiceImpl;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.OLD_PASSWORD_NOT_MATCHING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationDetailMasterDaoServiceTest {

    @InjectMocks
    RegistrationDetailMasterDaoServiceImpl registrationDetailMasterDaoService;
    @Mock
    private RegistrationDetailMasterRepository registrationDetailMasterRepository;

    @BeforeEach
    void setup() {
    }

    @Test
    void testFindByHpProfileId() {
        when(registrationDetailMasterRepository.getRegistrationDetailsByHpProfileId(any(BigInteger.class))).thenReturn(getMasterRegistrationDetails());
        RegistrationDetailsMaster response = registrationDetailMasterDaoService.findByHpProfileId(HP_ID);
        assertEquals(1, response.getId());
        assertEquals(REGISTRATION_NUMBER, response.getRegistrationNo());
        assertEquals(REGISTRATION_DATE, response.getRegistrationDate());
        assertEquals(ID, response.getHpProfileMaster().getId());
        assertEquals(ID, response.getStateMedicalCouncil().getId());
    }
}