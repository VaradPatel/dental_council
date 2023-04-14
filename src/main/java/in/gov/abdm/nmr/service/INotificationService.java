package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ResponseMessageTo;

/**
 * Interface to declare methods
 */
public interface INotificationService {

    ResponseMessageTo sendNotificationOnStatusChangeForHP(String applicationType, String action, String mobile, String email);

    ResponseMessageTo sendNotificationOnStatusChangeForCollege(String applicationType, String action, String mobile,String email);

    ResponseMessageTo sendNotificationForResetPasswordLink(String email, String link);

    ResponseMessageTo sendNotificationForOTP(String type, String otp, String receiver);

    ResponseMessageTo sendNotificationForVerifiedOTP(String type, String receiver);
    ResponseMessageTo sendNotificationForAccountCreation(String username,String mobile);
    ResponseMessageTo sendNotificationForNMRCreation(String username,String mobile, String email);

    ResponseMessageTo sendNotificationForEmailVerificationLink(String email,String link);

}
