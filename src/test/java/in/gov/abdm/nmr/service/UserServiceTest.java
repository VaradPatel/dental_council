package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.mapper.INbeMapper;
import in.gov.abdm.nmr.mapper.INmcMapper;
import in.gov.abdm.nmr.mapper.ISmcMapper;
import in.gov.abdm.nmr.repository.IFetchUserDetailsCustomRepository;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.repository.ResetTokenRepository;
import in.gov.abdm.nmr.service.impl.OtpServiceImpl;
import in.gov.abdm.nmr.service.impl.UserServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    IUserDaoService userDaoService;
    @Mock
    IUserRepository userDetailRepository;
    @Mock
    IFetchUserDetailsCustomRepository fetchUserDetailsCustomRepository;
    @Mock
    OtpServiceImpl otpService;
    @Mock
    ResetTokenRepository resetTokenRepository;
    @Mock
    ISmcMapper smcMapper;
    @Mock
    INmcMapper nmcMapper;
    @Mock
    INbeMapper nbeMapper;
    @Mock
    ISmcProfileRepository smcProfileRepository;
    @Mock
    INmcDaoService nmcDaoService;
    @Mock
    EntityManager entityManager;

    @Test
    void testToggleSmsNotification() {
        when(userDaoService.toggleSmsNotification(true)).thenReturn(getUser(UserTypeEnum.SMC.getId()));
        NotificationToggleResponseTO response = userService.toggleSmsNotification(true);
        assertEquals(CommonTestData.ID, response.getUserId());
        assertEquals(NMRConstants.SMS, response.getMode());
    }

    @Test
    void testToggleEmailNotification() {
        when(userDaoService.toggleEmailNotification(true)).thenReturn(getUser(UserTypeEnum.SMC.getId()));
        NotificationToggleResponseTO response = userService.toggleEmailNotification(true);
        assertEquals(CommonTestData.ID, response.getUserId());
        assertEquals(NMRConstants.EMAIL, response.getMode());
    }


    @Test
    void testToggleNotification() {
        when(userDaoService.toggleNotification(new NotificationToggleRequestTO())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        List<NotificationToggleResponseTO> response = userService.toggleNotification(new NotificationToggleRequestTO());
        assertEquals(2, response.size());
        assertEquals(CommonTestData.ID, response.get(0).getUserId());
        assertEquals(NMRConstants.SMS, response.get(0).getMode());
    }

    static UserResponseTO getUserResponse() {
        UserResponseTO response = new UserResponseTO();
        List<UserTO> userTOList = new ArrayList<>();
        UserTO user = new UserTO();
        user.setId(CommonTestData.ID);
        user.setUserTypeId(UserSubTypeEnum.NMC_ADMIN.getId());
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmailId(CommonTestData.EMAIL_ID);
        user.setMobileNumber(MOBILE_NUMBER);
        userTOList.add(user);
        response.setUsers(userTOList);
        response.setTotalNoOfRecords(CommonTestData.ID);
        return response;
    }

    @Test
    void testGetAllUserShouldGetUserDetailsBasedOnLoginUserAuthoritySearchByMobileNumber() throws AccessDeniedException, InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailRepository.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.NMC.getId()));
        when(fetchUserDetailsCustomRepository.fetchUserData(any(UserRequestParamsTO.class), any(Pageable.class))).thenReturn(getUserResponse());
        UserResponseTO user = userService.getAllUser(MOBILE_NUMBER_IN_LOWER_CASE, MOBILE_NUMBER, 1, 1, "", "");
        assertEquals(CommonTestData.ID, user.getTotalNoOfRecords());
        assertEquals(CommonTestData.ID, user.getUsers().get(0).getId());
        assertEquals(UserSubTypeEnum.NMC_ADMIN.getId(), user.getUsers().get(0).getUserTypeId());
        assertEquals(FIRST_NAME, user.getUsers().get(0).getFirstName());
        assertEquals(LAST_NAME, user.getUsers().get(0).getLastName());
        assertEquals(CommonTestData.EMAIL_ID, user.getUsers().get(0).getEmailId());
        assertEquals(MOBILE_NUMBER, user.getUsers().get(0).getMobileNumber());
    }

    @Test
    void testGetAllUserShouldGetUserDetailsBasedOnLoginUserAuthoritySearchByUserTypeID() throws AccessDeniedException, InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailRepository.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.NMC.getId()));
        when(fetchUserDetailsCustomRepository.fetchUserData(any(UserRequestParamsTO.class), any(Pageable.class))).thenReturn(getUserResponse());
        UserResponseTO user = userService.getAllUser(USER_TYPE_ID_IN_LOWER_CASE, UserTypeEnum.NBE.getId().toString(), 1, 1, "", "");
        assertEquals(CommonTestData.ID, user.getTotalNoOfRecords());
        assertEquals(CommonTestData.ID, user.getUsers().get(0).getId());
        assertEquals(UserSubTypeEnum.NMC_ADMIN.getId(), user.getUsers().get(0).getUserTypeId());
        assertEquals(FIRST_NAME, user.getUsers().get(0).getFirstName());
        assertEquals(LAST_NAME, user.getUsers().get(0).getLastName());
        assertEquals(CommonTestData.EMAIL_ID, user.getUsers().get(0).getEmailId());
        assertEquals(MOBILE_NUMBER, user.getUsers().get(0).getMobileNumber());
    }

    @Test
    void testGetAllUserShouldGetUserDetailsBasedOnLoginUserAuthoritySearchByFirstName() throws AccessDeniedException, InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailRepository.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.NMC.getId()));
        when(fetchUserDetailsCustomRepository.fetchUserData(any(UserRequestParamsTO.class), any(Pageable.class))).thenReturn(getUserResponse());
        UserResponseTO user = userService.getAllUser(FIRST_NAME_IN_LOWER_CASE, FIRST_NAME, 1, 1, "", "");
        assertEquals(CommonTestData.ID, user.getTotalNoOfRecords());
        assertEquals(CommonTestData.ID, user.getUsers().get(0).getId());
        assertEquals(UserSubTypeEnum.NMC_ADMIN.getId(), user.getUsers().get(0).getUserTypeId());
        assertEquals(FIRST_NAME, user.getUsers().get(0).getFirstName());
        assertEquals(LAST_NAME, user.getUsers().get(0).getLastName());
        assertEquals(CommonTestData.EMAIL_ID, user.getUsers().get(0).getEmailId());
        assertEquals(MOBILE_NUMBER, user.getUsers().get(0).getMobileNumber());
    }


    @Test
    void testGetAllUserShouldGetUserDetailsBasedOnLoginUserAuthoritySearchByLastName() throws AccessDeniedException, InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailRepository.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.NMC.getId()));
        when(fetchUserDetailsCustomRepository.fetchUserData(any(UserRequestParamsTO.class), any(Pageable.class))).thenReturn(getUserResponse());
        UserResponseTO user = userService.getAllUser(LAST_NAME_IN_LOWER_CASE, FIRST_NAME, 1, 1, "", "");
        assertEquals(CommonTestData.ID, user.getTotalNoOfRecords());
        assertEquals(CommonTestData.ID, user.getUsers().get(0).getId());
        assertEquals(UserSubTypeEnum.NMC_ADMIN.getId(), user.getUsers().get(0).getUserTypeId());
        assertEquals(FIRST_NAME, user.getUsers().get(0).getFirstName());
        assertEquals(LAST_NAME, user.getUsers().get(0).getLastName());
        assertEquals(CommonTestData.EMAIL_ID, user.getUsers().get(0).getEmailId());
        assertEquals(MOBILE_NUMBER, user.getUsers().get(0).getMobileNumber());
    }

    @Test
    void testGetAllUserShouldGetUserDetailsBasedOnLoginUserAuthoritySearchByEmailID() throws AccessDeniedException, InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailRepository.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.NMC.getId()));
        when(fetchUserDetailsCustomRepository.fetchUserData(any(UserRequestParamsTO.class), any(Pageable.class))).thenReturn(getUserResponse());
        UserResponseTO user = userService.getAllUser(EMAIL_ID_IN_LOWER_CASE, FIRST_NAME, 1, 1, "", "");
        assertEquals(CommonTestData.ID, user.getTotalNoOfRecords());
        assertEquals(CommonTestData.ID, user.getUsers().get(0).getId());
        assertEquals(UserSubTypeEnum.NMC_ADMIN.getId(), user.getUsers().get(0).getUserTypeId());
        assertEquals(FIRST_NAME, user.getUsers().get(0).getFirstName());
        assertEquals(LAST_NAME, user.getUsers().get(0).getLastName());
        assertEquals(CommonTestData.EMAIL_ID, user.getUsers().get(0).getEmailId());
        assertEquals(MOBILE_NUMBER, user.getUsers().get(0).getMobileNumber());
    }

    @Test
    void testDeactivateUserShouldChangeStatusAsDeactivateForUser() {
        doNothing().when(userDetailRepository).deactivateUser(any(BigInteger.class));
        userService.deactivateUser(CommonTestData.USER_ID);
        verify(userDetailRepository, times(1)).deactivateUser(any(BigInteger.class));
    }

    static RetrieveUserRequestTo getRetrieveUserRequest() {
        RetrieveUserRequestTo retrieveUserRequestTo = new RetrieveUserRequestTo();
        retrieveUserRequestTo.setTransactionId(TRANSACTION_ID);
        retrieveUserRequestTo.setContact(MOBILE_NUMBER);
        return retrieveUserRequestTo;
    }

    @Test
    void testRetrieveUserShouldValidateOtpAndReturnEmailID() throws OtpException {
        when(otpService.isOtpVerified(anyString())).thenReturn(false);
        when(userDaoService.findFirstByMobileNumber(anyString())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        String response = userService.retrieveUser(getRetrieveUserRequest());
        assertEquals(CommonTestData.EMAIL_ID, response);
    }

    static User getRetrieveUserName() {
        User user = new User();
        user.setUserName(TEST_USER);
        return user;
    }

    @Test
    void testRetrieveUserShouldValidateOtpAndReturnUsername() throws OtpException {
        when(otpService.isOtpVerified(anyString())).thenReturn(false);
        when(userDaoService.findFirstByMobileNumber(anyString())).thenReturn(getRetrieveUserName());
        String response = userService.retrieveUser(getRetrieveUserRequest());
        assertEquals(TEST_USER, response);
    }

    @Test
    void testRetrieveUserShouldThrowInvalidOtpException() {
        when(otpService.isOtpVerified(anyString())).thenReturn(true);
        assertThrows(OtpException.class, () -> userService.retrieveUser(getRetrieveUserRequest()));
    }

    @Test
    void testVerifyEmailShouldValidateLinkAndReturnSuccessResponse() throws InvalidRequestException{
        when(resetTokenRepository.findByToken(anyString())).thenReturn(getResetToken());
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(userDaoService.save(any(User.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        ResponseMessageTo responseMessageTo = userService.verifyEmail(new VerifyEmailTo(TEMP_TOKEN));
        assertEquals(SUCCESS_RESPONSE, responseMessageTo.getMessage());
    }

    @Test
    void testVerifyEmailShouldTokenValueNullAndReturnLinkExpired() throws InvalidRequestException{
        when(resetTokenRepository.findByToken(anyString())).thenReturn(null);
        ResponseMessageTo responseMessageTo = userService.verifyEmail(new VerifyEmailTo(TEMP_TOKEN));
        assertEquals(LINK_EXPIRED, responseMessageTo.getMessage());
    }

    public ResetToken getExpiredResetToken() {
        ResetToken resetToken = new ResetToken();
        resetToken.setId(CommonTestData.USER_ID);
        resetToken.setExpiryDate(PAST_TIMESTAMP);
        resetToken.setUserName(TEST_USER);
        return resetToken;
    }

    @Test
    void testVerifyEmailShouldReturnLinkExpired() throws InvalidRequestException{
        when(resetTokenRepository.findByToken(anyString())).thenReturn(getExpiredResetToken());
        ResponseMessageTo responseMessageTo = userService.verifyEmail(new VerifyEmailTo(TEMP_TOKEN));
        assertEquals(LINK_EXPIRED, responseMessageTo.getMessage());
    }


    public static UserProfileTO getUserProfileForNMRException() {
        UserProfileTO userProfileTO = new UserProfileTO();
        userProfileTO.setSmcId(SMC_ID);
        userProfileTO.setName(SMC_NAME);
        userProfileTO.setTypeId(UserTypeEnum.NMC.getId());
        userProfileTO.setSubTypeId(UserTypeEnum.HEALTH_PROFESSIONAL.getId());
        userProfileTO.setEmailId(CommonTestData.EMAIL_ID);
        userProfileTO.setMobileNumber(MOBILE_NUMBER);
        return userProfileTO;
    }

    @Test
    void testCreateUserShouldThrowNmrException() {
        assertThrows(NmrException.class, () -> userService.createUser(getUserProfileForNMRException()));
    }

    @Test
    void testUnlockUserShouldUnlockUser() {
        doNothing().when(userDaoService).unlockUser(any(BigInteger.class));
        userService.unlockUser(BigInteger.valueOf(1));
        verify(userDaoService, times(1)).unlockUser(any(BigInteger.class));
    }

    @Test
    void testGetSmcProfile() throws NmrException, InvalidIdException {
        when(userDaoService.findSmcProfile(any(BigInteger.class))).thenReturn(getSmcProfile());
        when(smcMapper.smcProfileToDto(any(SMCProfile.class))).thenReturn(getSMCProfileTo());
        SMCProfileTO smcProfile = userService.getSmcProfile(CommonTestData.ID);
        assertEquals(CommonTestData.ID, smcProfile.getId());
    }

    @Test
    void testGetNmcProfile() throws NmrException, InvalidIdException {
        when(userDaoService.findNmcProfile(any(BigInteger.class))).thenReturn(getNmcProfile());
        when(nmcMapper.nmcProfileToDto(any(NmcProfile.class))).thenReturn(getNmcProfileTO());
        NmcProfileTO nmcProfile = userService.getNmcProfile(CommonTestData.ID);
        assertEquals(CommonTestData.ID, nmcProfile.getId());
    }

    @Test
    void testGetNbeProfile() throws NmrException, InvalidIdException {
        when(userDaoService.findNbeProfile(any(BigInteger.class))).thenReturn(getNbeProfile());
        when(nbeMapper.nbeProfileToDto(any(NbeProfile.class))).thenReturn(getNbeProfileTO());
        NbeProfileTO nbeProfile = userService.getNbeProfile(CommonTestData.ID);
        assertEquals(CommonTestData.ID, nbeProfile.getId());
    }

    @Test
    void testUpdateSmcProfile() throws NmrException, InvalidIdException, InvalidRequestException {
        when(userDaoService.updateSmcProfile(any(BigInteger.class), any(SMCProfileTO.class))).thenReturn(getSmcProfile());
        when(smcMapper.smcProfileToDto(any(SMCProfile.class))).thenReturn(getSMCProfileTo());
        SMCProfileTO smcProfileTO = userService.updateSmcProfile(CommonTestData.ID, getSMCProfileTo());
        assertEquals(CommonTestData.ID, smcProfileTO.getId());

    }

    @Test
    void testUpdateNmcProfile() throws NmrException, InvalidIdException, InvalidRequestException {
        when(userDaoService.updateNmcProfile(any(BigInteger.class), any(NmcProfileTO.class))).thenReturn(getNmcProfile());
        when(nmcMapper.nmcProfileToDto(any(NmcProfile.class))).thenReturn(getNmcProfileTO());
        NmcProfileTO nmcProfileTO = userService.updateNmcProfile(CommonTestData.ID, getNmcProfileTO());
        assertEquals(CommonTestData.ID, nmcProfileTO.getId());
    }

    @Test
    void testUpdateNbeProfile() throws NmrException, InvalidIdException, InvalidRequestException {
        when(userDaoService.updateNbeProfile(any(BigInteger.class), any(NbeProfileTO.class))).thenReturn(getNbeProfile());
        when(nbeMapper.nbeProfileToDto(any(NbeProfile.class))).thenReturn(getNbeProfileTO());
        NbeProfileTO nbeProfileTO = userService.updateNbeProfile(CommonTestData.ID, getNbeProfileTO());
        assertEquals(CommonTestData.ID, nbeProfileTO.getId());

    }
}