package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.NmcProfile;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.repository.INmcProfileRepository;
import in.gov.abdm.nmr.service.impl.NmcDaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NmcDaoServiceTest {

    @Mock
    INmcProfileRepository nmcProfileRepository;

    @InjectMocks
    NmcDaoServiceImpl nmcDaoService;

    NmcProfile nmcProfile =  new NmcProfile();
    User user =  null;
    StateMedicalCouncil stateMedicalCouncil = null;

    @BeforeEach
    void setup(){
        user = getUser(UserTypeEnum.NATIONAL_MEDICAL_COUNCIL.getCode());
        stateMedicalCouncil =  getStateMedicalCouncil();
        populateNmcProfile();
    }

    @Test
    void testFindByUserIdReturnsNmcProfile(){
        when(nmcProfileRepository.findByUserId(any(BigInteger.class))).thenReturn(nmcProfile);
        NmcProfile nmcProfile = nmcDaoService.findByUserId(ID);
        assertEquals(ID, nmcProfile.getId());
        assertEquals(PROFILE_DISPLAY_NAME, nmcProfile.getDisplayName());
        assertEquals(EMAIL_ID, nmcProfile.getEmailId());
        assertEquals(FIRST_NAME, nmcProfile.getFirstName());
        assertEquals(LAST_NAME, nmcProfile.getLastName());
        assertEquals(MIDDLE_NAME, nmcProfile.getMiddleName());
        assertEquals(ENROLLED_NUMBER, nmcProfile.getEnrolledNumber());
        assertEquals(NDHM_ENROLLMENT_NUMBER, nmcProfile.getNdhmEnrollment());
        assertEquals(MOBILE_NUMBER, nmcProfile.getMobileNo());
        assertEquals(user, nmcProfile.getUser());
    }

    private void populateNmcProfile() {
        nmcProfile.setId(ID);
        nmcProfile.setDisplayName(PROFILE_DISPLAY_NAME);
        nmcProfile.setEmailId(EMAIL_ID);
        nmcProfile.setFirstName(FIRST_NAME);
        nmcProfile.setLastName(LAST_NAME);
        nmcProfile.setMiddleName(MIDDLE_NAME);
        nmcProfile.setEnrolledNumber(ENROLLED_NUMBER);
        nmcProfile.setNdhmEnrollment(NDHM_ENROLLMENT_NUMBER);
        nmcProfile.setMobileNo(MOBILE_NUMBER);
        nmcProfile.setUser(user);
    }




}
