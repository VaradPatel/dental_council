package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileMaster;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.impl.OtpServiceImpl;
import in.gov.abdm.nmr.service.impl.UserServiceImpl;
import in.gov.abdm.nmr.service.impl.WorkflowPostProcessorServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    private ISmcMapper smcMapper;
    @Mock
    private INmcMapper nmcMapper;
    @Mock
    private INbeMapper nbeMapper;

    @Mock
    private IUserDaoService userDaoService;

    @Mock
    private EntityManager entityManager;

    @Mock
    RsaUtil rsaUtil;

    @Mock
    IHpProfileRepository hpProfileRepository;

    @Mock
    private IPasswordDaoService passwordDaoService;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    INotificationService notificationService;

    @Mock
    OtpServiceImpl otpService;

    @Mock
    IRegistrationDetailRepository iRegistrationDetailRepository;

    @Mock
    private ResetTokenRepository resetTokenRepository;

    @Test
    void testToggleSmsNotification() {
        when(userDaoService.toggleSmsNotification(true)).thenReturn(getUser(UserTypeEnum.SMC.getId()));
        NotificationToggleResponseTO response = userService.toggleSmsNotification(true);
        assertEquals(ID, response.getUserId());
        assertEquals(NMRConstants.SMS, response.getMode());
    }

    @Test
    void testToggleEmailNotification() {
        when(userDaoService.toggleEmailNotification(true)).thenReturn(getUser(UserTypeEnum.SMC.getId()));
        NotificationToggleResponseTO response = userService.toggleEmailNotification(true);
        assertEquals(ID, response.getUserId());
        assertEquals(NMRConstants.EMAIL, response.getMode());
    }


    @Test
    void testToggleNotification() {
        when(userDaoService.toggleNotification(new NotificationToggleRequestTO())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        List<NotificationToggleResponseTO> response = userService.toggleNotification(new NotificationToggleRequestTO());
        assertEquals(2, response.size());
        assertEquals(ID, response.get(0).getUserId());
        assertEquals(NMRConstants.SMS, response.get(0).getMode());
    }


}