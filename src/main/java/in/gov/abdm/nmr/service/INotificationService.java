package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ResponseMessageTo;

/**
 * Interface to declare methods
 */
public interface INotificationService {

    ResponseMessageTo sendNotificationOnStatusChangeForHP(String applicationType, String action, String mobile, String email);

    ResponseMessageTo sendNotificationOnStatusChangeForCollege(String applicationType, String action, String mobile,String email);

    ResponseMessageTo sendNotificationForResetPasswordLink(String type, String receiver, String link);

    ResponseMessageTo sendNotificationForOTP(String type, String otp, String receiver);

    ResponseMessageTo sendNotificationForVerifiedOTP(String type, String receiver);

}
