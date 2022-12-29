package in.gov.abdm.nmr.service;
import com.fasterxml.jackson.core.JsonProcessingException;

import in.gov.abdm.nmr.dto.NotificationDataTo;

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
