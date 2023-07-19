package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.NotificationToggleRequestTO;
import in.gov.abdm.nmr.dto.NotificationToggleTO;
import in.gov.abdm.nmr.entity.NbeProfile;
import in.gov.abdm.nmr.entity.NmcProfile;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.repository.INbeProfileRepository;
import in.gov.abdm.nmr.repository.INmcProfileRepository;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.impl.UserDaoServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoServiceTest {

    @InjectMocks
    UserDaoServiceImpl userDaoService;
    @Mock
    private IUserRepository userDetailRepository;
    @Mock
    private ISmcProfileRepository smcProfileRepository;
    @Mock
    private INmcProfileRepository nmcProfileRepository;
    @Mock
    private INbeProfileRepository nbeProfileRepository;
    @Mock
    private EntityManager entityManager;
    @Mock
    IAccessControlService accessControlService;

    @Test
    void testFindById() {
        when(userDetailRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getUser(UserTypeEnum.SMC.getId())));
        User user = userDaoService.findById(ID);
        assertEquals(ID, user.getId());
        assertEquals(EMAIL_ID, user.getEmail());
        assertEquals(true, user.isAccountNonLocked());
        assertEquals(0, user.getFailedAttempt());
    }

    @Test
    void testUnlockUserShouldUnlockUser() {
        doNothing().when(userDetailRepository).unlockUser(any(BigInteger.class));
        userDaoService.unlockUser(BigInteger.valueOf(1));
        verify(userDetailRepository, times(1)).unlockUser(any(BigInteger.class));
    }

    @Test
    void testSaveShouldSaveUser() {
        when(userDetailRepository.saveAndFlush(any(User.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        User user = userDaoService.save(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        assertEquals(ID, user.getId());
    }

    @Test
    void testFindByUsernameShouldFindUserDetailsByUserName() {
        when(userDetailRepository.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        User user = userDaoService.findByUsername(TEST_USER, TEST_USER_TYPE);
        assertEquals(ID, user.getId());
    }

    @Test
    void testFindFirstByMobileNumberShouldFindUserDetailsByMobileNumber() {
        when(userDetailRepository.findFirstByMobileNumber(anyString())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        User user = userDaoService.findFirstByMobileNumber(MOBILE_NUMBER);
        assertEquals(ID, user.getId());
    }

    @Test
    void testExistsByUserNameShouldCheckRecordAreExistForUserName() {
        when(userDetailRepository.existsByUserNameAndUserTypeId(anyString(), any(BigInteger.class))).thenReturn(true);
        boolean isExist = userDaoService.existsByUserNameAndUserTypeId(TEST_USER, TEST_USER_TYPE);
        assertTrue(isExist);
    }

    @Test
    void testExistsByMobileNumberShouldCheckRecordAreExistForMobileNumber() {
        when(userDetailRepository.existsByMobileNumberAndUserTypeId(anyString(), any(BigInteger.class))).thenReturn(true);
        boolean isExist = userDaoService.existsByMobileNumberAndUserTypeId(MOBILE_NUMBER, TEST_USER_TYPE);
        assertTrue(isExist);
    }

    @Test
    void testExistsByEmailShouldCheckRecordAreExistForEmail() {
        when(userDetailRepository.existsByEmailAndUserTypeId(anyString(), any(BigInteger.class))).thenReturn(true);
        boolean isExist = userDaoService.existsByEmailAndUserTypeId(EMAIL_ID, TEST_USER_TYPE);
        assertTrue(isExist);
    }

    @Test
    void testToggleSmsNotification() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailRepository.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(userDetailRepository.saveAndFlush(any(User.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        User user = userDaoService.toggleSmsNotification(true);
        assertEquals(ID, user.getId());
    }

    @Test
    void testToggleEmailNotification() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailRepository.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(userDetailRepository.saveAndFlush(any(User.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        User user = userDaoService.toggleEmailNotification(true);
        assertEquals(ID, user.getId());
    }

    public static NotificationToggleRequestTO getNotificationToggleRequestForSMSMode() {
        NotificationToggleRequestTO notificationToggleRequestTO = new NotificationToggleRequestTO();
        List<NotificationToggleTO> notificationToggles = new ArrayList<>();
        NotificationToggleTO notificationToggleTO = new NotificationToggleTO();
        notificationToggleTO.setIsEnabled(true);
        notificationToggleTO.setMode(NMRConstants.SMS);
        notificationToggles.add(notificationToggleTO);
        notificationToggleRequestTO.setNotificationToggles(notificationToggles);
        return notificationToggleRequestTO;
    }

    public static NotificationToggleRequestTO getNotificationToggleRequestForEmailMode() {
        NotificationToggleRequestTO notificationToggleRequestTO = new NotificationToggleRequestTO();
        List<NotificationToggleTO> notificationToggles = new ArrayList<>();
        NotificationToggleTO notificationToggleTO = new NotificationToggleTO();
        notificationToggleTO.setIsEnabled(true);
        notificationToggleTO.setMode(NMRConstants.EMAIL);
        notificationToggles.add(notificationToggleTO);
        notificationToggleRequestTO.setNotificationToggles(notificationToggles);
        return notificationToggleRequestTO;
    }

    @Test
    void testToggleNotificationShouldToggleNotificationForSMSMode() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailRepository.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(userDetailRepository.saveAndFlush(any(User.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        User user = userDaoService.toggleNotification(getNotificationToggleRequestForSMSMode());
        assertEquals(ID, user.getId());
    }

    @Test
    void testToggleNotificationShouldToggleNotificationForEmailMode() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailRepository.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.COLLEGE.getId()));
        when(userDetailRepository.saveAndFlush(any(User.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        User user = userDaoService.toggleNotification(getNotificationToggleRequestForEmailMode());
        assertEquals(ID, user.getId());
    }

    @Test
    void testFindSmcProfile() throws InvalidIdException {
        when(smcProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getSmcProfile()));
        doNothing().when(accessControlService).validateUser(any(BigInteger.class));
        SMCProfile smcProfile = userDaoService.findSmcProfile(ID);
        assertEquals(ID, smcProfile.getId());
    }

    @Test
    void testFindSmcProfileShouldThrowInvalidIdException() {
        assertThrows(InvalidIdException.class, () -> userDaoService.findSmcProfile(ID));
    }

    @Test
    void testFindNmcProfile() throws InvalidIdException {
        when(nmcProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getNmcProfile()));
        doNothing().when(accessControlService).validateUser(any(BigInteger.class));
        NmcProfile nmcProfile = userDaoService.findNmcProfile(ID);
        assertEquals(ID, nmcProfile.getId());
    }

    @Test
    void testFindNmcProfileShouldThrowInvalidIdException() {
        assertThrows(InvalidIdException.class, () -> userDaoService.findNmcProfile(ID));
    }

    @Test
    void testFindNbeProfile() throws InvalidIdException {
        when(nbeProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getNbeProfile()));
        doNothing().when(accessControlService).validateUser(any(BigInteger.class));
        NbeProfile nbeProfile = userDaoService.findNbeProfile(ID);
        assertEquals(ID, nbeProfile.getId());
    }

    @Test
    void testFindNbeProfileShouldThrowInvalidIdException() {
        assertThrows(InvalidIdException.class, () -> userDaoService.findNbeProfile(ID));
    }

    @Test
    void testUpdateSmcProfile() throws InvalidIdException, InvalidRequestException {
        when(smcProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getSmcProfile()));
        when(smcProfileRepository.save(any(SMCProfile.class))).thenReturn(getSmcProfile());
        SMCProfile smcProfile = userDaoService.updateSmcProfile(ID, getSMCProfileTo());
        assertEquals(ID, smcProfile.getId());
    }

    @Test
    void testUpdateSmcProfileShouldThrowInvalidIdException() throws InvalidIdException {
        assertThrows(InvalidIdException.class, () -> userDaoService.updateSmcProfile(ID, getSMCProfileTo()));
    }


    @Test
    void testUpdateNmcProfile() throws InvalidIdException, InvalidRequestException {
        when(nmcProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getNmcProfile()));
        when(nmcProfileRepository.saveAndFlush(any(NmcProfile.class))).thenReturn(getNmcProfile());
        NmcProfile nmcProfile = userDaoService.updateNmcProfile(ID, getNmcProfileTO());
        assertEquals(ID, nmcProfile.getId());
    }

    @Test
    void testUpdateNmcProfileShouldThrowInvalidIdException() {
        assertThrows(InvalidIdException.class, () -> userDaoService.updateNmcProfile(ID, getNmcProfileTO()));
    }

    @Test
    void testUpdateNbeProfile() throws InvalidIdException, InvalidRequestException {
        when(nbeProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getNbeProfile()));
        when(nbeProfileRepository.saveAndFlush(any(NbeProfile.class))).thenReturn(getNbeProfile());
        NbeProfile nmcProfile = userDaoService.updateNbeProfile(ID, getNbeProfileTO());
        assertEquals(ID, nmcProfile.getId());
    }

    @Test
    void testUpdateNbeProfileShouldThrowInvalidIdException() {
        assertThrows(InvalidIdException.class, () -> userDaoService.updateNbeProfile(ID, getNbeProfileTO()));
    }

    @Test
    void testCheckEmailUsedByOtherUser() {
        when(userDetailRepository.checkEmailUsedByOtherUser(any(BigInteger.class), anyString(), any(BigInteger.class))).thenReturn(true);
        boolean usedByOtherUser = userDaoService.checkEmailUsedByOtherUser(ID, EMAIL_ID, TEST_USER_TYPE);
        assertTrue(usedByOtherUser);
    }

}