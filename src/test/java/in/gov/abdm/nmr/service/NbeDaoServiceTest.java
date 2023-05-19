package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.NbeProfile;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.repository.INbeProfileRepository;
import in.gov.abdm.nmr.service.impl.NbeDaoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NbeDaoServiceTest {

    @Mock
    INbeProfileRepository nbeProfileRepository;

    @InjectMocks
    NbeDaoServiceImpl nbeDaoService;

    @Test
    void testFindByUserIdShouldReturnNbeProfile() {
        when(nbeProfileRepository.findByUserId(any(BigInteger.class))).thenReturn(getNbeProfile());
        NbeProfile nbeProfile = nbeDaoService.findByUserId(ID);
        assertEquals(ID, nbeProfile.getId());
        assertEquals(PROFILE_DISPLAY_NAME, nbeProfile.getDisplayName());
        assertEquals(FIRST_NAME, nbeProfile.getFirstName());
        assertEquals(LAST_NAME, nbeProfile.getLastName());
        assertEquals(EMAIL_ID, nbeProfile.getEmailId());
        assertEquals(MOBILE_NUMBER, nbeProfile.getMobileNo());

    }

    private NbeProfile getNbeProfile() {
        NbeProfile nbeProfile = new NbeProfile();
        nbeProfile.setDisplayName(PROFILE_DISPLAY_NAME);
        nbeProfile.setId(ID);
        nbeProfile.setFirstName(FIRST_NAME);
        nbeProfile.setLastName(LAST_NAME);
        nbeProfile.setMobileNo(MOBILE_NUMBER);
        nbeProfile.setUser(getUser(UserTypeEnum.NBE.getId()));
        nbeProfile.setEmailId(EMAIL_ID);
        return nbeProfile;
    }

    @Test
    void testSave() {
        when(nbeProfileRepository.save(any(NbeProfile.class))).thenReturn(getNbeProfile());
        NbeProfile nbeProfile = nbeDaoService.save(getNbeProfile());
        assertEquals(ID, nbeProfile.getId());
        assertEquals(PROFILE_DISPLAY_NAME, nbeProfile.getDisplayName());
        assertEquals(FIRST_NAME, nbeProfile.getFirstName());
        assertEquals(LAST_NAME, nbeProfile.getLastName());
        assertEquals(EMAIL_ID, nbeProfile.getEmailId());
        assertEquals(MOBILE_NUMBER, nbeProfile.getMobileNo());
    }
}
