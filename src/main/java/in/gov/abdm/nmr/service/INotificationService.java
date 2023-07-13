package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.exception.TemplateException;

/**
 * Interface to declare methods
 */
public interface INotificationService {

    ResponseMessageTo sendNotificationOnStatusChangeForHP(String applicationType, String action, String mobile, String email) throws TemplateException;

    ResponseMessageTo sendNotificationForResetPasswordLink(String email, String link, String username) throws TemplateException;

    ResponseMessageTo sendNotificationForOTP(String type, String otp, String receiver) throws TemplateException;

    ResponseMessageTo sendNotificationForVerifiedOTP(String type, String receiver) throws TemplateException;

    ResponseMessageTo sendNotificationForAccountCreation(String username,String mobile) throws TemplateException;

    ResponseMessageTo sendNotificationForNMRCreation(String nmrId,String mobile) throws TemplateException;

    ResponseMessageTo sendNotificationForEmailVerificationLink(String email,String link) throws TemplateException;

    ResponseMessageTo sendNotificationForHprAccountCreation(String username,String hprId, String mobile) throws TemplateException;

}