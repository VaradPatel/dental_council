package in.gov.abdm.nmr.api.controller.notification;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.gov.abdm.nmr.api.constant.NMRConstants;
import in.gov.abdm.nmr.api.controller.notification.to.KeyValue;
import in.gov.abdm.nmr.api.controller.notification.to.NotificationDataTo;
import in.gov.abdm.nmr.api.controller.notification.to.NotificationRequestTo;

/**
 * Implementations of methods to send OTP on SMS and Email
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired(required = true)
    RestTemplate restTemplateDisableSSL;

    @Autowired
    ObjectMapper mapper;

    @Value("${global.notification.endpoint}")
    private String notificationEndpoint;

    @Value("${notification.origin}")
    private String notificationOrigin;

    @Value("${notification.sender}")
    private String notificationSender;

    /**
     * Sends OTP on Email
     * @param notificationDataTo coming from OTP Service
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public String sendOTPOnEmail(NotificationDataTo notificationDataTo) throws JsonProcessingException {
        NotificationRequestTo notificationRequestTo = new NotificationRequestTo();
        notificationRequestTo.setOrigin(notificationOrigin);
        notificationRequestTo.setContentType(notificationDataTo.getContentType());
        notificationRequestTo.setSender(notificationSender);
        notificationRequestTo.setType(List.of(NotificationType.EMAIL.getNotificationType()));
        KeyValue receiver = new KeyValue();
        receiver.setKey(NMRConstants.EMAIL_ID);
        receiver.setValue(notificationDataTo.getEmailId());
        notificationRequestTo.setReceiver(List.of(receiver));

        KeyValue templateKeyValue = new KeyValue();
        templateKeyValue.setKey(NMRConstants.TEMPLATE_ID);
        templateKeyValue.setValue(notificationDataTo.getTemplateId());
        KeyValue subjectKeyValue = new KeyValue();
        subjectKeyValue.setKey(NMRConstants.SUBJECT);
        subjectKeyValue.setValue(notificationDataTo.getSubject());
        KeyValue contentKeyValue = new KeyValue();
        contentKeyValue.setKey(NMRConstants.CONTENT);
        contentKeyValue.setValue(notificationDataTo.getContent());
        notificationRequestTo.setNotification(List.of(templateKeyValue, subjectKeyValue, contentKeyValue));
        return sendNotification(notificationRequestTo);
    }

    /**
     * Sends OTP on SMS
     * @param notificationDataTo coming from OTP Service
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public String sendOTPOnMobile(NotificationDataTo notificationDataTo) throws JsonProcessingException {
        NotificationRequestTo notificationRequestTo = new NotificationRequestTo();
        notificationRequestTo.setOrigin(notificationOrigin);
        notificationRequestTo.setContentType(notificationDataTo.getContentType());
        notificationRequestTo.setSender(notificationSender);
        notificationRequestTo.setType(List.of(NotificationType.SMS.getNotificationType()));
        KeyValue receiver = new KeyValue();
        receiver.setKey(NMRConstants.MOBILE);
        receiver.setValue(notificationDataTo.getMobileNumber());
        notificationRequestTo.setReceiver(List.of(receiver));
        KeyValue templateKeyValue = new KeyValue();
        templateKeyValue.setKey(NMRConstants.TEMPLATE_ID);
        templateKeyValue.setValue(notificationDataTo.getTemplateId());
        KeyValue contentKeyValue = new KeyValue();
        contentKeyValue.setKey(NMRConstants.CONTENT);
        contentKeyValue.setValue(notificationDataTo.getContent());
        notificationRequestTo.setNotification(List.of(templateKeyValue, contentKeyValue));
        return sendNotification(notificationRequestTo);
    }

    /**
     * Calls external API to send OTP
     * @param notificationRequestTo created to hit API
     * @return
     * @throws JsonProcessingException
     */
    private String sendNotification(NotificationRequestTo notificationRequestTo) throws JsonProcessingException {

        String jsonString = mapper.writeValueAsString(notificationRequestTo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(NMRConstants.TIMESTAMP, Timestamp.valueOf(LocalDateTime.now()).toString());
        HttpEntity<String> dscRequest = new HttpEntity<String>(jsonString.toString(), headers);
        restTemplateDisableSSL.postForEntity(notificationEndpoint+NMRConstants.NOTIFICATION_SERVICE_SEND_MESSAGE, dscRequest, NotificationRequestTo.class);
        return NMRConstants.SUCCESS_RESPONSE;
    }
}

