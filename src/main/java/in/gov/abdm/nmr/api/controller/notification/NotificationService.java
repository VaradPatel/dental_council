package in.gov.abdm.nmr.api.controller.notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.api.controller.notification.to.NotificationDataTo;

/**
 * Declaration of methods
 */
public interface NotificationService {

    /**
     * Sends OTP on Email
     */
    String sendOTPOnEmail(NotificationDataTo notificationDataTo) throws  JsonProcessingException;

    /**
     * Sends OTP on SMS
     */
    String sendOTPOnMobile(NotificationDataTo notificationDataTo) throws  JsonProcessingException;
}
