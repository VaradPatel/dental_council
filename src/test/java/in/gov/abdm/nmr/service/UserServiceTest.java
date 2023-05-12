package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.repository.IFetchUserDetailsCustomRepository;
import in.gov.abdm.nmr.repository.IUserRepository;
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

import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    private IUserDaoService userDaoService;
    @Mock
    private IUserRepository userDetailRepository;
    @Mock
    IFetchUserDetailsCustomRepository fetchUserDetailsCustomRepository;

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

    public static UserResponseTO getUserResponse() {
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
    public void testGetAllUserShouldGetUserDetailsBasedOnLoginUserAuthoritySearchByMobileNumber() throws AccessDeniedException, InvalidRequestException {
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
    public void testGetAllUserShouldGetUserDetailsBasedOnLoginUserAuthoritySearchByUserTypeID() throws AccessDeniedException, InvalidRequestException {
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
    public void testGetAllUserShouldGetUserDetailsBasedOnLoginUserAuthoritySearchByFirstName() throws AccessDeniedException, InvalidRequestException {
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
    public void testGetAllUserShouldGetUserDetailsBasedOnLoginUserAuthoritySearchByLastName() throws AccessDeniedException, InvalidRequestException {
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
    public void testGetAllUserShouldGetUserDetailsBasedOnLoginUserAuthoritySearchByEmailID() throws AccessDeniedException, InvalidRequestException {
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
        userDetailRepository.deactivateUser(any(BigInteger.class));
        userService.deactivateUser(CommonTestData.USER_ID);
    }
}