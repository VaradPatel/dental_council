package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import in.gov.abdm.nmr.client.NotificationDBFClient;
import in.gov.abdm.nmr.client.NotificationFClient;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.TemplateException;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.util.TemplatedStringBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.enums.NotificationType;

/**
 * Implementation of methods to generate and validate OTP
 */
@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    NotificationFClient notificationFClient;

    @Autowired
    NotificationDBFClient notificationDBFClient;

    @Autowired
    MessageSource messageSource;

    @Value("${notification.origin}")
    private String notificationOrigin;

    @Value("${notification.sender}")
    private String notificationSender;

    /**
     * Sends notification on each status change
     *
     * @param type type sms/email
     * @param otp to be sent on email/sms
     * @param receiver to send sms/sms
     * @return success/failure
     */
    @Override
    public ResponseMessageTo sendNotificationForOTP(String type, String otp, String receiver) {

        if (NotificationType.EMAIL.getNotificationType().equals(type)) {

            Template template = getMessageTemplate(NMRConstants.EMAIL_OTP_MESSAGE_PROPERTIES_KEY);

            String message = new TemplatedStringBuilder(template.getMessage())
                    .replace(NMRConstants.TEMPLATE_VAR1, otp)
                    .replace(NMRConstants.TEMPLATE_VAR2, receiver)
                    .replace(NMRConstants.TEMPLATE_VAR3, NMRConstants.MESSAGE_SENDER)
                    .finish();

            return sendNotification(List.of(NotificationType.EMAIL.getNotificationType()), NMRConstants.OTP_CONTENT_TYPE, template.getId().toString(), NMRConstants.OTP_EMAIL_SUBJECT, message, receiver, receiver);

        } else if (NotificationType.SMS.getNotificationType().equals(type)) {

            Template template = getMessageTemplate(NMRConstants.SMS_OTP_MESSAGE_PROPERTIES_KEY);

            String message = new TemplatedStringBuilder(template.getMessage())
                    .replace(NMRConstants.TEMPLATE_VAR1, otp)
                    .replace(NMRConstants.TEMPLATE_VAR2, receiver.substring(receiver.length() - 4))
                    .replace(NMRConstants.TEMPLATE_VAR3, NMRConstants.MESSAGE_SENDER)
                    .finish();

            return sendNotification(List.of(NotificationType.SMS.getNotificationType()), NMRConstants.OTP_CONTENT_TYPE, template.getId().toString(), NMRConstants.OTP_EMAIL_SUBJECT, message, receiver,receiver);
        }
        return new ResponseMessageTo(NMRConstants.NO_SUCH_OTP_TYPE);
    }

    /**
     * Sends notification on each status change
     *
     * @param type type sms/email
     * @param receiver to send sms/sms
     * @return success/failure
     */
    @Override
    public ResponseMessageTo sendNotificationForVerifiedOTP(String type, String receiver) {

        if (NotificationType.EMAIL.getNotificationType().equals(type)) {


            Template template = getMessageTemplate(NMRConstants.EMAIL_VERIFIED_MESSAGE_PROPERTIES_KEY);

            String message = new TemplatedStringBuilder(template.getMessage())
                    .replace(NMRConstants.TEMPLATE_VAR1, receiver)
                    .replace(NMRConstants.TEMPLATE_VAR2, NMRConstants.MESSAGE_SENDER)
                    .finish();

            sendNotification(List.of(NotificationType.EMAIL.getNotificationType()), NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.INFO_EMAIL_SUBJECT, message, receiver,receiver);

        } else if (NotificationType.SMS.getNotificationType().equals(type)) {
            Template template = getMessageTemplate(NMRConstants.SMS_VERIFIED_MESSAGE_PROPERTIES_KEY);

            String message = new TemplatedStringBuilder(template.getMessage())
                    .replace(NMRConstants.TEMPLATE_VAR1, receiver.substring(receiver.length() - 4))
                    .replace(NMRConstants.TEMPLATE_VAR2, NMRConstants.MESSAGE_SENDER)
                    .finish();

            sendNotification(List.of(NotificationType.SMS.getNotificationType()), NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.INFO_EMAIL_SUBJECT, message, receiver, receiver);
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
    public ResponseMessageTo sendNotificationOnStatusChangeForHP(String applicationType, String action, String email, String mobile) {

        Template template = getMessageTemplate(NMRConstants.STATUS_CHANGED_MESSAGE_PROPERTIES_KEY);

        String message = new TemplatedStringBuilder(template.getMessage())
                .replace(NMRConstants.TEMPLATE_VAR1, applicationType)
                .replace(NMRConstants.TEMPLATE_VAR2, action)
                .replace(NMRConstants.TEMPLATE_VAR3, NMRConstants.MESSAGE_SENDER)
                .finish();

        return sendNotification(List.of(NotificationType.SMS.getNotificationType(), NotificationType.EMAIL.getNotificationType()), NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.INFO_EMAIL_SUBJECT, message, mobile, email);

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
    public ResponseMessageTo sendNotificationOnStatusChangeForCollege(String applicationType, String action, String email, String mobile) {

        Template template = getMessageTemplate(NMRConstants.STATUS_CHANGED_MESSAGE_PROPERTIES_KEY);

        String message = new TemplatedStringBuilder(template.getMessage())
                .replace(NMRConstants.TEMPLATE_VAR1, applicationType)
                .replace(NMRConstants.TEMPLATE_VAR2, action)
                .replace(NMRConstants.TEMPLATE_VAR3, NMRConstants.MESSAGE_SENDER)
                .finish();

        return sendNotification(List.of(NotificationType.SMS.getNotificationType(), NotificationType.EMAIL.getNotificationType()), NMRConstants.INFO_CONTENT_TYPE, template.getId().toString(), NMRConstants.INFO_EMAIL_SUBJECT, message, mobile, email);
    }

    /**
     * Sends notification on each status change
     *
     * @param type sms/email
     * @param receiver to send message/email
     * @param link link for reset password
     * @return success/failure
     */
    @Override
    public ResponseMessageTo sendNotificationForResetPasswordLink(String type, String receiver, String link) {

        if (NotificationType.EMAIL.getNotificationType().equals(type)) {
            Template template = getMessageTemplate(NMRConstants.EMAIL_OTP_MESSAGE_PROPERTIES_KEY);
            String message = link;
            return sendNotification(List.of(NotificationType.EMAIL.getNotificationType()), NMRConstants.OTP_CONTENT_TYPE, template.getId().toString(), NMRConstants.OTP_EMAIL_SUBJECT, message, receiver, receiver);

        } else if (NotificationType.SMS.getNotificationType().equals(type)) {
            Template template = getMessageTemplate(NMRConstants.SMS_OTP_MESSAGE_PROPERTIES_KEY);
            String message = link;
            return sendNotification(List.of(NotificationType.SMS.getNotificationType()), NMRConstants.OTP_CONTENT_TYPE, template.getId().toString(), NMRConstants.OTP_EMAIL_SUBJECT, message, receiver,receiver);
        }
        return null;
    }

    @SneakyThrows
    Template getMessageTemplate(String propertiesKey) {
        Template template;
        try {
            String templateId = messageSource.getMessage(propertiesKey.toLowerCase(), null, Locale.ENGLISH);
            template = notificationDBFClient.getTemplateById(new BigInteger(templateId));

        } catch (NoSuchMessageException noSuchMessageException) {
            throw new TemplateException(NMRConstants.TEMPLATE_ID_NOT_FOUND_IN_PROPERTIES);
        }

        if (null == template) {
            throw new TemplateException(NMRConstants.TEMPLATE_NOT_FOUND);
        }
        return template;
    }

    ResponseMessageTo sendNotification(List<String> type, String contentType, String templateId, String subject, String content, String mobile, String email) {

        NotificationRequestTo notificationRequestTo = new NotificationRequestTo();
        notificationRequestTo.setOrigin(notificationOrigin);
        notificationRequestTo.setSender(notificationSender);
        notificationRequestTo.setContentType(contentType);
        notificationRequestTo.setType(type);

        if (type.contains(NMRConstants.SMS) && type.contains(NMRConstants.EMAIL)) {

            KeyValue receiverMobile = new KeyValue();
            receiverMobile.setKey(NMRConstants.MOBILE);
            receiverMobile.setValue(mobile);

            KeyValue receiverEmail = new KeyValue();
            receiverEmail.setKey(NMRConstants.EMAIL_ID);
            receiverEmail.setValue(email);
            notificationRequestTo.setReceiver(List.of(receiverMobile, receiverEmail));

        } else if (type.contains(NMRConstants.SMS)) {

            KeyValue receiverMobile = new KeyValue();
            receiverMobile.setKey(NMRConstants.MOBILE);
            receiverMobile.setValue(mobile);
            notificationRequestTo.setReceiver(List.of(receiverMobile));
        } else if (type.contains(NMRConstants.EMAIL)) {

            KeyValue receiverEmail = new KeyValue();
            receiverEmail.setKey(NMRConstants.EMAIL_ID);
            receiverEmail.setValue(email);
            notificationRequestTo.setReceiver(List.of(receiverEmail));
        }

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

        System.out.println(notificationRequestTo);

        NotificationResponseTo response;
        try {
            response = notificationFClient.sendNotification(notificationRequestTo, Timestamp.valueOf(LocalDateTime.now()), "123");

        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
        return new ResponseMessageTo(response.status().equalsIgnoreCase(NMRConstants.SENT_RESPONSE) ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE);
    }
}
