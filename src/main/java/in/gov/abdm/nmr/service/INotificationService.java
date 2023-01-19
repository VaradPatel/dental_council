package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.*;

/**
 * Interface to declare methods
 */
public interface INotificationService {

    ResponseMessageTo sendNotificationOnStatusChangeForHP(String applicationType, String action, String email, String mobile);

    ResponseMessageTo sendNotificationOnStatusChangeForCollege(String applicationType, String action, String email, String mobile);

    ResponseMessageTo sendNotificationForResetPasswordLink(String type, String receiver, String link);

    ResponseMessageTo sendNotificationForOTP(String type, String otp, String receiver);

    ResponseMessageTo sendNotificationForVerifiedOTP(String type, String receiver);

}
