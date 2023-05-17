package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.OTPResponseMessageTo;
import in.gov.abdm.nmr.dto.OtpGenerateRequestTo;
import in.gov.abdm.nmr.enums.NotificationType;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.redis.hash.Otp;
import in.gov.abdm.nmr.service.impl.OtpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.SUCCESS_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @InjectMocks
    OtpServiceImpl otpService;
    @Mock
    IOtpDaoService otpDaoService;

    @Mock
    IUserDaoService userDaoService;

    @Mock
    INotificationService notificationService;


    @BeforeEach
    void setup() {
    }

    @Test
    void testGenerateOtp() throws OtpException {
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(otpDaoService.findAllByContact(anyString())).thenReturn(geOtpList());
        when(otpDaoService.save(any(Otp.class))).thenReturn(getOtp());
        when(notificationService.sendNotificationForOTP(anyString(), anyString(), anyString())).thenReturn(getResponseMessage());
        OTPResponseMessageTo otpResponseMessageTo = otpService.generateOtp(otpGenerateRequest());
        assertEquals(SUCCESS_RESPONSE, otpResponseMessageTo.getMessage());
        assertEquals(MOBILE_NUMBER, otpResponseMessageTo.getSentOn());
    }

}