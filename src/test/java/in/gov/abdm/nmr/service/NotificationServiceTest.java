package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.client.NotificationDBFClient;
import in.gov.abdm.nmr.client.NotificationFClient;
import in.gov.abdm.nmr.enums.NotificationType;
import in.gov.abdm.nmr.repository.INotificationRepository;
import in.gov.abdm.nmr.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import static in.gov.abdm.nmr.util.CommonTestData.MOBILE_NUMBER;
import static in.gov.abdm.nmr.util.CommonTestData.OTP;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    NotificationServiceImpl notificationService;
    @Mock
    NotificationFClient notificationFClient;
    @Mock
    NotificationDBFClient notificationDBFClient;
    @Mock
    MessageSource messageSource;
    @Mock
    INotificationRepository iNotificationRepository;
    @Value("${notification.origin}")
    private String notificationOrigin;
    @Value("${notification.sender}")
    private String notificationSender;

    @BeforeEach
    void setup() {
    }

    @Test
    void testSendNotificationForOTPShouldThrowException() {
        assertThrows(Exception.class, () -> notificationService.sendNotificationForOTP(NotificationType.EMAIL.getNotificationType(), OTP, MOBILE_NUMBER));
    }

}