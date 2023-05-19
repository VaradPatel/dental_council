package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.HpWorkProfileUpdateRequestTO;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.service.impl.SmcProfileDaoServiceImpl;
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
class SmcProfileDaoServiceTest {
    @Mock
    ISmcProfileRepository smcProfileRepository;

    @InjectMocks
    SmcProfileDaoServiceImpl smcProfileService;

    SMCProfile smcProfile = new SMCProfile();
    User user = null;
    StateMedicalCouncil stateMedicalCouncil = null;


    @BeforeEach
    void setup() {
        user = getUser(UserTypeEnum.SMC.getId());
        stateMedicalCouncil = getStateMedicalCouncil();
        populateSmcProfile();
    }

    @Test
    void testFindByUserIdReturnsNmcProfile() {
        when(smcProfileRepository.findByUserId(any(BigInteger.class))).thenReturn(smcProfile);
        SMCProfile smcProfile = smcProfileService.findByUserId(ID);
        assertEquals(ID, smcProfile.getId());
        assertEquals(PROFILE_DISPLAY_NAME, smcProfile.getDisplayName());
        assertEquals(EMAIL_ID, smcProfile.getEmailId());
        assertEquals(FIRST_NAME, smcProfile.getFirstName());
        assertEquals(LAST_NAME, smcProfile.getLastName());
        assertEquals(MIDDLE_NAME, smcProfile.getMiddleName());
        assertEquals(ENROLLED_NUMBER, smcProfile.getEnrolledNumber());
        assertEquals(NDHM_ENROLLMENT_NUMBER, smcProfile.getNdhmEnrollment());
        assertEquals(MOBILE_NUMBER, smcProfile.getMobileNo());
        assertEquals(user, smcProfile.getUser());
    }

    private void populateSmcProfile() {
        smcProfile.setId(ID);
        smcProfile.setDisplayName(PROFILE_DISPLAY_NAME);
        smcProfile.setEmailId(EMAIL_ID);
        smcProfile.setFirstName(FIRST_NAME);
        smcProfile.setLastName(LAST_NAME);
        smcProfile.setMiddleName(MIDDLE_NAME);
        smcProfile.setEnrolledNumber(ENROLLED_NUMBER);
        smcProfile.setNdhmEnrollment(NDHM_ENROLLMENT_NUMBER);
        smcProfile.setMobileNo(MOBILE_NUMBER);
        smcProfile.setUser(user);
    }

    @Test
    void testSave() {
        when(smcProfileRepository.save(any(SMCProfile.class))).thenReturn(getSmcProfile());
        SMCProfile profile = smcProfileService.save(getSmcProfile());
        assertEquals(ID,profile.getId());
        assertEquals(ID,profile.getStateMedicalCouncil().getId());
    }
}
