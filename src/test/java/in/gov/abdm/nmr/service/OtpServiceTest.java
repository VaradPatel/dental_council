package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.OTPResponseMessageTo;
import in.gov.abdm.nmr.dto.OtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.OtpValidateRequestTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.enums.NotificationType;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.exception.TemplateException;
import in.gov.abdm.nmr.redis.hash.Otp;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.impl.OtpServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.FAILURE_RESPONSE;
import static in.gov.abdm.nmr.util.NMRConstants.SUCCESS_RESPONSE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {
    @Mock
    IOtpDaoService otpDaoService;
    @Mock
    IUserDaoService userDaoService;
    @Mock
    INotificationService notificationService;
    @Mock
    RsaUtil rsaUtil;

    @Mock
    IOtpValidationService otpValidationService;

    @InjectMocks
    OtpServiceImpl otpService = new OtpServiceImpl();

    @BeforeEach
    void setup() {
    }

    @Test
    void testGenerateOtpShouldGenerateOtpAndSendToMobileNumberForNMRId() throws OtpException, InvalidRequestException, TemplateException {
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(otpDaoService.findAllByContact(anyString())).thenReturn(geOtpList());
        when(otpDaoService.save(any(Otp.class))).thenReturn(getOtp());
        when(notificationService.sendNotificationForOTP(anyString(), anyString(), anyString())).thenReturn(getResponseMessage());
        OTPResponseMessageTo otpResponseMessageTo = otpService.generateOtp(otpGenerateRequest());
        assertEquals(SUCCESS_RESPONSE, otpResponseMessageTo.getMessage());
        assertEquals(MOBILE_NUMBER, otpResponseMessageTo.getSentOn());
    }

    @Test
    void testGenerateOtpShouldGenerateSMSAndSendToMobileNumber() throws OtpException, InvalidRequestException, TemplateException {
        when(otpDaoService.findAllByContact(anyString())).thenReturn(geOtpList());
        when(otpDaoService.save(any(Otp.class))).thenReturn(getOtp());
        when(notificationService.sendNotificationForOTP(anyString(), anyString(), anyString())).thenReturn(getResponseMessage());
        OTPResponseMessageTo otpResponseMessageTo = otpService.generateOtp(new OtpGenerateRequestTo(MOBILE_NUMBER, UserTypeEnum.HEALTH_PROFESSIONAL.getId(), NotificationType.SMS.getNotificationType(), true));
        assertEquals(SUCCESS_RESPONSE, otpResponseMessageTo.getMessage());
    }

    @Test
    void testGenerateOtpShouldGenerateEmailAndSendToMobileNumber() throws OtpException, InvalidRequestException, TemplateException {
        when(otpDaoService.findAllByContact(anyString())).thenReturn(geOtpList());
        when(otpDaoService.save(any(Otp.class))).thenReturn(getOtp());
        when(notificationService.sendNotificationForOTP(anyString(), anyString(), anyString())).thenReturn(getResponseMessage());
        OTPResponseMessageTo otpResponseMessageTo = otpService.generateOtp(new OtpGenerateRequestTo(EMAIL_ID, UserTypeEnum.HEALTH_PROFESSIONAL.getId(), NotificationType.EMAIL.getNotificationType(), true));
        assertEquals(SUCCESS_RESPONSE, otpResponseMessageTo.getMessage());
    }

    @Test
    void testGenerateOtpShouldReturnFailureResponseWhenNotificationFail() throws OtpException, InvalidRequestException, TemplateException {
        when(otpDaoService.findAllByContact(anyString())).thenReturn(geOtpList());
        when(otpDaoService.save(any(Otp.class))).thenReturn(getOtp());
        when(notificationService.sendNotificationForOTP(anyString(), anyString(), anyString())).thenReturn(new ResponseMessageTo(NMRConstants.NO_SUCH_OTP_TYPE));
        OTPResponseMessageTo otpResponseMessageTo = otpService.generateOtp(new OtpGenerateRequestTo(EMAIL_ID, UserTypeEnum.HEALTH_PROFESSIONAL.getId(), NotificationType.EMAIL.getNotificationType(), true));
        assertEquals(FAILURE_RESPONSE, otpResponseMessageTo.getMessage());
    }

    @Test
    void testGenerateOtpShouldThrowOtpAttemptsExceededWhenPreviousOtpAreMoreThanFiveTimes() {
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(otpDaoService.findAllByContact(anyString())).thenReturn(List.of(getOtp(), getOtp(), getOtp(), getOtp(), getOtp()));
        assertThrows(OtpException.class, () -> otpService.generateOtp(otpGenerateRequest()));
    }

    @Test
    void testGenerateOtpShouldThrowOtpAttemptsExceededForOtpMaxAttempts() {
        when(userDaoService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(otpDaoService.findAllByContact(anyString())).thenReturn(List.of(getOtpMaxAttempts()));
        assertThrows(OtpException.class, () -> otpService.generateOtp(otpGenerateRequest()));
    }

    @Test
    void testValidateOtp() throws OtpException, GeneralSecurityException, TemplateException {
        when(otpDaoService.findById(anyString())).thenReturn(getOtp());
        when(otpDaoService.save(any(Otp.class))).thenReturn(getOtp());
        when(notificationService.sendNotificationForVerifiedOTP(anyString(), anyString())).thenReturn(getResponseMessage());
        otpService.validateOtp(getOtpValidateRequest(), true);
    }

    @Test
    void testValidateOtpShouldThrowExceptionOtpExpired() {
        when(otpDaoService.findById(anyString())).thenReturn(null);
        assertThrows(OtpException.class, () -> otpService.validateOtp(getOtpValidateRequest(), true));
    }

    @Test
    void testValidateOtpShouldThrowOtpAttemptsExceededForOtpMaxAttempts() {
        when(otpDaoService.findById(anyString())).thenReturn(getOtpMaxAttempts());
        assertThrows(OtpException.class, () -> otpService.validateOtp(getOtpValidateRequest(), true));
    }

    public static OtpValidateRequestTo getOtpValidateRequestForInvalidOtp() {
        OtpValidateRequestTo otpValidateRequestTo = new OtpValidateRequestTo();
        otpValidateRequestTo.setTransactionId(TRANSACTION_ID);
        otpValidateRequestTo.setContact(MOBILE_NUMBER);
        otpValidateRequestTo.setOtp("887652");
        otpValidateRequestTo.setType(NotificationType.SMS.getNotificationType());
        return otpValidateRequestTo;
    }

    @Test
    void testValidateOtpShouldValidateOtpAndReturnInvalidOtp() {
        when(otpDaoService.findById(anyString())).thenReturn(getOtp());
        when(otpDaoService.save(any(Otp.class))).thenReturn(getOtp());
        assertThrows(OtpException.class, () -> otpService.validateOtp(getOtpValidateRequestForInvalidOtp(), true));
    }

    public static Otp getOtpForExpired() {
        Otp otp = new Otp();
        otp.setId("1");
        otp.setAttempts(1);
        otp.setExpired(true);
        otp.setOtp(OTP);
        return otp;
    }
}