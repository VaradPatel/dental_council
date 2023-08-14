package in.gov.abdm.nmr.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import in.gov.abdm.nmr.client.NotificationDBFClient;
import in.gov.abdm.nmr.client.NotificationFClient;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.Notification;
import in.gov.abdm.nmr.enums.NotificationType;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.exception.TemplateException;
import in.gov.abdm.nmr.repository.INotificationRepository;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.TemplatedStringBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Implementation of methods to generate and validate OTP
 */
@Slf4j
@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    NotificationFClient notificationFClient;

    @Autowired
    NotificationDBFClient notificationDBFClient;

    @Autowired
    MessageSource messageSource;

    @Autowired
    INotificationRepository iNotificationRepository;

    @Value("${notification.origin}")
    private String notificationOrigin;

    @Value("${notification.sender}")
    private String notificationSender;

    /**
     * Sends notification on each status change
     *
     * @param type     type sms/email
     * @param otp      to be sent on email/sms
     * @param receiver to send sms/sms
     * @return success/failure
     */
    @Override
    public ResponseMessageTo sendNotificationForOTP(String type, String otp, String receiver) throws TemplateException {

        if (NotificationType.EMAIL.getNotificationType().equals(type)) {

            Template template = getMessageTemplate(NMRConstants.EMAIL_OTP_MESSAGE_PROPERTIES_KEY);

            String message = new TemplatedStringBuilder(template.getMessage())
                    .replace(NMRConstants.TEMPLATE_VAR1, otp)
                    .replace(NMRConstants.TEMPLATE_VAR2, receiver)
                    .replace(NMRConstants.TEMPLATE_VAR3, NMRConstants.MESSAGE_SENDER)
                    .finish();

            return sendNotification(NMRConstants.OTP_CONTENT_TYPE, template.getId().toString(), NMRConstants.OTP_EMAIL_SUBJECT, message, null, receiver,NMRConstants.EMAIL_OTP_MESSAGE_PROPERTIES_KEY);

        } else if (NotificationType.SMS.getNotificationType().equals(type)) {

            Template template = getMessageTemplate(NMRConstants.SMS_OTP_MESSAGE_PROPERTIES_KEY);

            String message = new TemplatedStringBuilder(template.getMessage())
                    .replace(NMRConstants.TEMPLATE_VAR1, otp)
                    .replace(NMRConstants.TEMPLATE_VAR2, receiver.substring(receiver.length() - 4))
                    .replace(NMRConstants.TEMPLATE_VAR3, NMRConstants.MESSAGE_SENDER)
                    .finish();

            return sendNotification(NMRConstants.OTP_CONTENT_TYPE, template.getId().toString(), NMRConstants.OTP_EMAIL_SUBJECT, message, receiver, null, NMRConstants.SMS_OTP_MESSAGE_PROPERTIES_KEY);
        }
        return new ResponseMessageTo(NMRConstants.NO_SUCH_OTP_TYPE);
    }

    /**
     * Sends notification on each status change
     *
     * @param type     type sms/email
     * @param receiver to send sms/sms
     * @return success/failure
     */
    @Override
    public ResponseMessageTo sendNotificationForVerifiedOTP(String type, String receiver) throws TemplateException {

        if (NotificationType.EMAIL.getNotificationType().equals(type)) {

            Template template = getMessageTemplate(NMRConstants.EMAIL_VERIFIED_MESSAGE_PROPERTIES_KEY);

            String message = new TemplatedStringBuilder(template.getMessage())
                    .replace(NMRConstants.TEMPLATE_VAR1, receiver)
                    .replace(NMRConstants.TEMPLATE_VAR2, NMRConstants.MESSAGE_SENDER)
                    .finish();

            return sendNotification(NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.INFO_EMAIL_VERIFICATION_SUCCESSFUL_SUBJECT, message, null, receiver, NMRConstants.EMAIL_VERIFIED_MESSAGE_PROPERTIES_KEY);

        } else if (NotificationType.SMS.getNotificationType().equals(type)) {
            Template template = getMessageTemplate(NMRConstants.SMS_VERIFIED_MESSAGE_PROPERTIES_KEY);

            String message = new TemplatedStringBuilder(template.getMessage())
                    .replace(NMRConstants.TEMPLATE_VAR1, receiver.substring(receiver.length() - 4))
                    .replace(NMRConstants.TEMPLATE_VAR2, NMRConstants.MESSAGE_SENDER)
                    .finish();

            return sendNotification(NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.INFO_EMAIL_SUBJECT, message, receiver, null, NMRConstants.SMS_VERIFIED_MESSAGE_PROPERTIES_KEY);
        }
        return new ResponseMessageTo(NMRConstants.NO_SUCH_OTP_TYPE);
    }

    /**
     * Sends notification on each status change
     *
     * @param applicationType what action performed
     * @param action          what action performed
     * @param mobile          mobile number to send message
     * @param email           to send email
     * @return success/failure
     */
    @Override
    public ResponseMessageTo sendNotificationOnStatusChangeForHP(String applicationType, String action, String mobile, String email) throws TemplateException {

        Template template = getMessageTemplate(NMRConstants.STATUS_CHANGED_MESSAGE_PROPERTIES_KEY);

        String message = new TemplatedStringBuilder(template.getMessage())
                .replace(NMRConstants.TEMPLATE_VAR1, applicationType)
                .replace(NMRConstants.TEMPLATE_VAR2, action)
                .replace(NMRConstants.TEMPLATE_VAR3, NMRConstants.MESSAGE_SENDER)
                .finish();

        return sendNotification(NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.INFO_EMAIL_SUBJECT, message, mobile, email, NMRConstants.STATUS_CHANGED_MESSAGE_PROPERTIES_KEY);

    }

    /**
     * Sends notification on each status change
     *
     * @param email email
     * @param link  link for reset password
     * @return success/failure
     */
    @Override
    public ResponseMessageTo sendNotificationForResetPasswordLink(String email, String link, String username) throws TemplateException {

        Template template = getMessageTemplate(NMRConstants.SMS_AND_EMAIL_RESET_MESSAGE_PROPERTIES_KEY);
        String message = new TemplatedStringBuilder(template.getMessage())
                .replace(NMRConstants.TEMPLATE_VAR1, username)
                .replace(NMRConstants.TEMPLATE_VAR2, link)
                .finish();
        return sendNotification(NMRConstants.OTP_CONTENT_TYPE, template.getId().toString(), NMRConstants.INFO_EMAIL_SET_PASSWORD_SUBJECT, message, null, email, NMRConstants.SMS_AND_EMAIL_RESET_MESSAGE_PROPERTIES_KEY);

    }

    @Override
    public ResponseMessageTo sendNotificationForAccountCreation(String username, String mobile) throws TemplateException {

        Template template = getMessageTemplate(NMRConstants.ACCOUNT_CREATED);
        String message = new TemplatedStringBuilder(template.getMessage())
                .replace(NMRConstants.TEMPLATE_VAR1, username)
                .replace(NMRConstants.TEMPLATE_VAR2, NMRConstants.NMR_ACCOUNT)
                .finish();
        return sendNotification(NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.ACCOUNT_CREATED_SUBJECT, message, mobile, null, NMRConstants.ACCOUNT_CREATED);

    }

    @Override
    public ResponseMessageTo sendNotificationForNMRCreation(String nmrId, String mobile) throws TemplateException {

        Template template = getMessageTemplate(NMRConstants.NMR_ID_CREATED);
        String message = new TemplatedStringBuilder(template.getMessage())
                .replace(NMRConstants.TEMPLATE_VAR1, nmrId)
                .replace(NMRConstants.TEMPLATE_VAR2, NMRConstants.MESSAGE_SENDER)
                .finish();
        return sendNotification(NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.ACCOUNT_CREATED_SUBJECT, message, mobile, null, NMRConstants.NMR_ID_CREATED);

    }

    @Override
    public ResponseMessageTo sendNotificationForEmailVerificationLink(String email,String link) throws TemplateException {

        Template template = getMessageTemplate(NMRConstants.EMAIL_VERIFICATION_TEMPLATE);
        String message = new TemplatedStringBuilder(template.getMessage())
                .replace(NMRConstants.TEMPLATE_VAR1, link)
                .finish();
        return sendNotification(NMRConstants.OTP_CONTENT_TYPE, template.getId().toString(), NMRConstants.VERIFY_EMAIL_SUBJECT, message, null, email,NMRConstants.EMAIL_VERIFICATION_TEMPLATE);

    }

    @Override
    public ResponseMessageTo sendNotificationForHprAccountCreation(String username,String hprId, String mobile) throws TemplateException {

        Template template = getMessageTemplate(NMRConstants.HPR_ACCOUNT_CREATED);
        String message = new TemplatedStringBuilder(template.getMessage())
                .replace(NMRConstants.TEMPLATE_VAR1, username)
                .replace(NMRConstants.TEMPLATE_VAR2, hprId)
                .finish();
        return sendNotification(NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.ACCOUNT_CREATED_SUBJECT, message, mobile, null,NMRConstants.HPR_ACCOUNT_CREATED);

    }

    @Override
    public ResponseMessageTo sendNotificationForIncorrectESign(String name, String mobile, String email) throws TemplateException {

        Template template = getMessageTemplate(NMRConstants.INCORRECT_E_SIGNED);
        String message = new TemplatedStringBuilder(template.getMessage())
                .replace(NMRConstants.TEMPLATE_VAR1, name)
                .finish();
        return sendNotification(NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.INCORRECT_E_SIGNED_SUBJECT, message, mobile, email,NMRConstants.INCORRECT_E_SIGNED);

    }

    @Override
    public ResponseMessageTo sendNotificationForLicenceStatus(String status, String mobile, String email) throws TemplateException {

        Template template = getMessageTemplate(NMRConstants.LICENCE_STATUS);
        String message = new TemplatedStringBuilder(template.getMessage())
                .replace(NMRConstants.TEMPLATE_VAR1, status)
                .replace(NMRConstants.TEMPLATE_VAR2, NMRConstants.MESSAGE_SENDER)
                .finish();
        return sendNotification(NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.LICENCE_UPDATE_SUBJECT, message, mobile, email,NMRConstants.LICENCE_STATUS);

    }

    @SneakyThrows
    Template getMessageTemplate(String propertiesKey) {
        Template template;
        try {
            String templateId = messageSource.getMessage(propertiesKey.toLowerCase(), null, Locale.ENGLISH);
            template = notificationDBFClient.getTemplateById(Timestamp.valueOf(LocalDateTime.now()), UUID.randomUUID().toString(), new BigInteger(templateId));

        } catch (NoSuchMessageException noSuchMessageException) {
            log.error("Exception occurred while sending notification.", noSuchMessageException);
            throw new TemplateException(NMRError.NOT_FOUND_EXCEPTION.getCode(), NMRError.NOT_FOUND_EXCEPTION.getMessage());
        }

        if (null == template) {
            throw new TemplateException(NMRError.TEMPLATE_NOT_FOUND.getCode(), NMRError.TEMPLATE_NOT_FOUND.getMessage());
        }
        return template;
    }

    private ResponseMessageTo sendNotification(String contentType, String templateId, String subject, String content, String mobile, String email, String templateName) {

        List<String> type = new ArrayList<>();

        NotificationRequestTo notificationRequestTo = new NotificationRequestTo();
        notificationRequestTo.setOrigin(notificationOrigin);
        notificationRequestTo.setSender(notificationSender);
        notificationRequestTo.setContentType(contentType);

        if (mobile != null && email != null) {

            type.addAll(List.of(NotificationType.SMS.getNotificationType(), NotificationType.EMAIL.getNotificationType()));

            KeyValue receiverMobile = new KeyValue();
            receiverMobile.setKey(NMRConstants.MOBILE);
            receiverMobile.setValue(mobile);

            KeyValue receiverEmail = new KeyValue();
            receiverEmail.setKey(NMRConstants.EMAIL_ID);
            receiverEmail.setValue(email);
            notificationRequestTo.setReceiver(List.of(receiverMobile, receiverEmail));

        } else if (mobile != null) {

            type.add(NotificationType.SMS.getNotificationType());

            KeyValue receiverMobile = new KeyValue();
            receiverMobile.setKey(NMRConstants.MOBILE);
            receiverMobile.setValue(mobile);
            notificationRequestTo.setReceiver(List.of(receiverMobile));
        } else if (email != null) {

            type.add(NotificationType.EMAIL.getNotificationType());

            KeyValue receiverEmail = new KeyValue();
            receiverEmail.setKey(NMRConstants.EMAIL_ID);
            receiverEmail.setValue(email);
            notificationRequestTo.setReceiver(List.of(receiverEmail));
        }

        notificationRequestTo.setType(type);

        KeyValue templateKeyValue = new KeyValue();
        templateKeyValue.setKey(NMRConstants.TEMPLATE_ID);
        templateKeyValue.setValue(templateId);

        KeyValue subjectKeyValue = new KeyValue();
        subjectKeyValue.setKey(NMRConstants.SUBJECT);
        subjectKeyValue.setValue(subject);

        KeyValue contentKeyValue = new KeyValue();
        contentKeyValue.setKey(NMRConstants.CONTENT);
        contentKeyValue.setValue(content);

        notificationRequestTo.setNotification(List.of(templateKeyValue, subjectKeyValue, contentKeyValue));

        NotificationResponseTo response;
        try {

            String requestId = UUID.randomUUID().toString();
            ObjectMapper objectMapper = new ObjectMapper();
            log.info(objectMapper.writeValueAsString(notificationRequestTo));
            response = notificationFClient.sendNotification(notificationRequestTo, Timestamp.valueOf(LocalDateTime.now()), requestId);
            log.info(requestId + " " + response);

            for (String notificationType : type) {

                Notification notification = new Notification();
                if (notificationType.equalsIgnoreCase(NotificationType.SMS.getNotificationType())) {
                    notification.setType(NotificationType.SMS.getNotificationType());
                    notification.setReceiver(mobile);
                } else if (notificationType.equalsIgnoreCase(NotificationType.EMAIL.getNotificationType())) {
                    notification.setType(NotificationType.EMAIL.getNotificationType());
                    notification.setReceiver(email);
                }
                notification.setTemplateId(templateId);
                notification.setRequestId(requestId);
                notification.setTemplateName(templateName);
                notification.setMessage(content);
                notification.setStatus(response.status());
                iNotificationRepository.save(notification);
            }

        }
        catch (FeignException.TooManyRequests e) {
            log.error("Exception occurred while sending notification.", e);
            return new ResponseMessageTo(NMRError.EMAIL_ATTEMPTS_EXCEEDED.getMessage());
        }
        catch (Exception e) {
            log.error("Exception occurred while sending notification.", e);
            return new ResponseMessageTo(NMRError.NMR_EXCEPTION.getMessage());
        }
        return new ResponseMessageTo(response.status().equalsIgnoreCase(NMRConstants.SENT_RESPONSE) ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE);
    }
}
