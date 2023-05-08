package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ChangePasswordRequestTo;
import in.gov.abdm.nmr.dto.ResetPasswordRequestTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.entity.Password;
import in.gov.abdm.nmr.entity.ResetToken;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.WorkFlowAudit;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.repository.ResetTokenRepository;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.impl.PasswordServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordServiceTest {
    @InjectMocks
    PasswordServiceImpl passwordService;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    IUserDaoService userDaoService;
    @Mock
    private ResetTokenRepository resetTokenRepository;
    @Mock
    INotificationService notificationService;
    @Mock
    private EntityManager entityManager;
    @Mock
    RsaUtil rsaUtil;
    @Mock
    IHpProfileRepository hpProfileRepository;
    @Mock
    private IPasswordDaoService passwordDaoService;
    @Mock
    private IOtpService otpService;

    @Mock
    private IUserRepository userDetailRepository;

    @Mock
    private IOtpDaoService otpDaoService;

    @BeforeEach
    void setup() {
    }

    @Test
    void testGetResetPasswordLinkShouldValidateUserAndSendNotificationForResetPassword() {
        when(userDaoService.existsByEmail(anyString())).thenReturn(true);
        when(notificationService.sendNotificationForResetPasswordLink(anyString(), anyString())).thenReturn(CommonTestData.getResponseMessage());
        ResponseMessageTo resetPasswordLink = passwordService.getResetPasswordLink(getSendLinkOnMail());
        assertEquals(SUCCESS_RESPONSE, resetPasswordLink.getMessage());
    }

    @Test
    void testGetResetPasswordLinkShouldValidateUserAndReturnUserNotFound() {
        when(userDaoService.existsByEmail(anyString())).thenReturn(false);
        ResponseMessageTo resetPasswordLink = passwordService.getResetPasswordLink(getSendLinkOnMail());
        assertEquals(USER_NOT_FOUND, resetPasswordLink.getMessage());
    }

    public ResetToken getResetTokenAsExpiryDate() {
        ResetToken resetToken = new ResetToken();
        resetToken.setToken(TEMP_TOKN);
        resetToken.setId(CommonTestData.USER_ID);
        resetToken.setExpiryDate(PAST_TIMESTAMP);
        resetToken.setUserName(TEST_USER);
        return resetToken;
    }

    @Test
    void testSetNewPasswordShouldValidateUserAndSetNewPassword() {
        when(resetTokenRepository.findByToken(getSetNewPasswordTo().getToken())).thenReturn(getResetToken());
        when(userDaoService.findByUsername(TEST_USER)).thenReturn(new User());
        ResponseMessageTo responseMessage = passwordService.setNewPassword(getSetNewPasswordTo());
        assertEquals(SUCCESS_RESPONSE, responseMessage.getMessage());
    }

    @Test
    void testSetNewPasswordShouldValidateUserUserIsNotAvailableThenReturnUserNotFound() {
        when(resetTokenRepository.findByToken(getSetNewPasswordTo().getToken())).thenReturn(getResetToken());
        when(userDaoService.findByUsername(TEST_USER)).thenReturn(null);
        ResponseMessageTo responseMessage = passwordService.setNewPassword(getSetNewPasswordTo());
        assertEquals(USER_NOT_FOUND, responseMessage.getMessage());
    }

    @Test
    void testSetNewPasswordShouldValidateUserLinkExpiredThenReturnLinkExpired() {
        when(resetTokenRepository.findByToken(getSetNewPasswordTo().getToken())).thenReturn(getResetTokenAsExpiryDate());
        when(userDaoService.findByUsername(TEST_USER)).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));
        ResponseMessageTo responseMessage = passwordService.setNewPassword(getSetNewPasswordTo());
        assertEquals(LINK_EXPIRED, responseMessage.getMessage());
    }

    @Test
    void testSetNewPasswordShouldReturnErrorMessage() {
        when(resetTokenRepository.findByToken(getSetNewPasswordTo().getToken())).thenReturn(getResetToken());
        when(userDaoService.findByUsername(TEST_USER)).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));
        when(userDaoService.save(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));
        when(passwordDaoService.save(new Password())).thenReturn(new Password());
        ResponseMessageTo responseMessage = passwordService.setNewPassword(getSetNewPasswordTo());
        assertNotNull(responseMessage.getMessage());
    }

    @Test
    void testResetPasswordShouldValidateUserAndOtpThenResetPassword() throws OtpException, GeneralSecurityException, InvalidRequestException {
        when(otpService.isOtpVerified(anyString())).thenReturn(false);
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));
        ResponseMessageTo responseMessage = passwordService.resetPassword(new ResetPasswordRequestTo(TEST_USER, TEST_PSWD, TRANSACTION_ID));
        assertEquals(SUCCESS_RESPONSE, responseMessage.getMessage());
    }

/*    @Test
    void testChangePasswordReturnSuccess() throws GeneralSecurityException, InvalidRequestException {
        when(userDaoService.findById(any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        ResponseMessageTo responseMessage = passwordService.changePassword(new ChangePasswordRequestTo(CommonTestData.USER_ID, TEST_PSWD, TEST_PSWD));
        assertEquals(SUCCESS_RESPONSE, responseMessage.getMessage());
    }*/

    @Test
    void testChangePasswordShouldValidateUserIfUserNotFoundThenReturnUserNotFound() throws GeneralSecurityException, InvalidRequestException {
        when(userDaoService.findById(any(BigInteger.class))).thenReturn(null);
        ResponseMessageTo responseMessage = passwordService.changePassword(new ChangePasswordRequestTo(CommonTestData.USER_ID, TEST_PSWD, TEST_PSWD));
        assertEquals(USER_NOT_FOUND, responseMessage.getMessage());
    }

    @Test
    void testChangePasswordShouldValidateUserIfOldPasswordIsNotMatchingThenReturnOldPasswordNotMatching() throws GeneralSecurityException, InvalidRequestException {
        when(userDaoService.findById(any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        ResponseMessageTo responseMessage = passwordService.changePassword(new ChangePasswordRequestTo(CommonTestData.USER_ID, TEST_PSWD, TEST_PSWD));
        assertEquals(OLD_PASSWORD_NOT_MATCHING, responseMessage.getMessage());
    }

    @Test
    void testGenerateLinkShouldValidateUserAndGenerateLink() {
        when(resetTokenRepository.findByUserName(anyString())).thenReturn(getResetToken());
        String responseMessage = passwordService.generateLink(getSendLinkOnMail());
        assertNotNull(responseMessage);
    }
}