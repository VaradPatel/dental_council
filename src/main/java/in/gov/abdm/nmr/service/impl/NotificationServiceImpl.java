package in.gov.abdm.nmr.service.impl;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import in.gov.abdm.nmr.client.NotificationDBFClient;
import in.gov.abdm.nmr.client.NotificationFClient;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.WorkFlowStatus;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.IWorkFlowStatusRepository;
import in.gov.abdm.nmr.service.INotificationService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.enums.NotificationType;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.repository.NmrOtpRepository;
import in.gov.abdm.nmr.entity.Otp;
import in.gov.abdm.nmr.repository.OtpRepository;

/**
 * Implementation of methods to generate and validate OTP
 */
@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    NmrOtpRepository nmrOtpRepository;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    IHpProfileRepository hpProfileRepository;

    @Autowired
    IWorkFlowStatusRepository workFlowStatusRepository;

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
     * Generates OTP by validating with repository
     * @param otpGenerateRequestTo coming from controller
     * @return Success/Failure message
     * @throws NoSuchAlgorithmException
     * @throws OtpException
     */
    @Override
    public ResponseMessageTo generateOtp(OtpGenerateRequestTo otpGenerateRequestTo) throws NoSuchAlgorithmException, OtpException, JsonProcessingException {
        if (nmrOtpRepository.findOtpGeneratedInLast15Minutes(otpGenerateRequestTo.getContact()) >= NMRConstants.OTP_GENERATION_MAX_ATTEMPTS) {
            throw new OtpException(NMRConstants.OTP_GENERATION_EXCEEDED);
        }
        if (nmrOtpRepository.findOtpGeneratedInLast10MinutesWithExceededAttempts(otpGenerateRequestTo.getContact()) > 0) {
            throw new OtpException(NMRConstants.OTP_ATTEMPTS_EXCEEDED);
        }
        String otp = String.valueOf(new SecureRandom().nextInt(999999));
        if (nmrOtpRepository.doesValidDuplicateOtpExists(DigestUtils.sha256Hex(otp), otpGenerateRequestTo.getContact())) {
            otp = String.valueOf(new SecureRandom().nextInt(999999));
        }

        nmrOtpRepository.saveOtpDetails(DigestUtils.sha256Hex(otp), otpGenerateRequestTo.getContact());

        String templateId= messageSource.getMessage(NMRConstants.OTP_MESSAGES_PROPERTIES_KEY,null, Locale.ENGLISH);

        Template template=notificationDBFClient.getTemplateById(new BigInteger(templateId));


        NotificationRequestTo notificationRequestTo=new NotificationRequestTo();
        notificationRequestTo.setOrigin(notificationOrigin);
        notificationRequestTo.setSender(notificationSender);
        notificationRequestTo.setContentType(NMRConstants.OTP_CONTENT_TYPE);

        if (NotificationType.EMAIL.getNotificationType().equals(otpGenerateRequestTo.getType())) {

            notificationRequestTo.setType(List.of(NotificationType.EMAIL.getNotificationType()));
            KeyValue receiver = new KeyValue();
            receiver.setKey(NMRConstants.EMAIL_ID);
            receiver.setValue(otpGenerateRequestTo.getContact());
            notificationRequestTo.setReceiver(List.of(receiver));

            KeyValue templateKeyValue = new KeyValue();
            templateKeyValue.setKey(NMRConstants.TEMPLATE_ID);
            templateKeyValue.setValue(template.getId().toString());
            KeyValue subjectKeyValue = new KeyValue();
            subjectKeyValue.setKey(NMRConstants.SUBJECT);
            subjectKeyValue.setValue(NMRConstants.OTP_EMAIL_SUBJECT);
            KeyValue contentKeyValue = new KeyValue();
            contentKeyValue.setKey(NMRConstants.CONTENT);
            contentKeyValue.setValue(template.getMessage());
            notificationRequestTo.setNotification(List.of(templateKeyValue, subjectKeyValue, contentKeyValue));

            NotificationResponseTo response=notificationFClient.sendNotification(notificationRequestTo,Timestamp.valueOf(LocalDateTime.now()),"123");
            return new ResponseMessageTo(response.status().equalsIgnoreCase(NMRConstants.SENT_RESPONSE)?NMRConstants.SUCCESS_RESPONSE:NMRConstants.FAILURE_RESPONSE);

        } else if (NotificationType.SMS.getNotificationType().equals(otpGenerateRequestTo.getType())) {

            notificationRequestTo.setType(List.of(NotificationType.SMS.getNotificationType()));
            KeyValue receiver = new KeyValue();
            receiver.setKey(NMRConstants.MOBILE);
            receiver.setValue(otpGenerateRequestTo.getContact());
            notificationRequestTo.setReceiver(List.of(receiver));

            KeyValue templateKeyValue = new KeyValue();
            templateKeyValue.setKey(NMRConstants.TEMPLATE_ID);
            templateKeyValue.setValue(template.getId().toString());
            KeyValue contentKeyValue = new KeyValue();
            contentKeyValue.setKey(NMRConstants.CONTENT);
            contentKeyValue.setValue(template.getMessage());
            notificationRequestTo.setNotification(List.of(templateKeyValue, contentKeyValue));

            NotificationResponseTo response=notificationFClient.sendNotification(notificationRequestTo,Timestamp.valueOf(LocalDateTime.now()),"123");
            return new ResponseMessageTo(response.status().equalsIgnoreCase(NMRConstants.SENT_RESPONSE)?NMRConstants.SUCCESS_RESPONSE:NMRConstants.FAILURE_RESPONSE);

        } else {
            throw (new OtpException(NMRConstants.NO_SUCH_OTP_TYPE));
        }
    }

    /**
     * Validated generated OTP with repository
     * @param otpValidateRequestTo
     * @return Success/Failure message
     * @throws OtpException
     */
    @Override
    public OtpValidateResponseTo validateOtp(OtpValidateRequestTo otpValidateRequestTo) throws OtpException {
        Otp otpDetails = otpRepository.findOneByExpiredIsFalseAndContactIsAndOtpHashIs(otpValidateRequestTo.getContact(),
                DigestUtils.sha256Hex(otpValidateRequestTo.getOtp()));
        if (otpDetails != null) {
            if (otpDetails.getExpiresAt().before(Timestamp.valueOf(LocalDateTime.now()))) {
                otpDetails.setExpired(true);
                otpRepository.flush();
                throw new OtpException(NMRConstants.OTP_EXPIRED);
            } else {
                if (otpDetails.getAttempts() >= NMRConstants.OTP_MAX_ATTEMPTS) {
                    otpDetails.setExpired(true);
                    otpRepository.flush();
                    throw new OtpException(NMRConstants.OTP_ATTEMPTS_EXCEEDED);
                }
                otpDetails.setExpired(true);
                otpRepository.flush();
                return new OtpValidateResponseTo(new OtpValidateMessageTo(NMRConstants.SUCCESS_RESPONSE, otpDetails.getId(), otpValidateRequestTo.getType()));
            }
        }
        Otp validOtpForContact = otpRepository.findOneByExpiredIsFalseAndContactIs(otpValidateRequestTo.getContact());
        if (validOtpForContact != null) {
            if (validOtpForContact.getAttempts() >= NMRConstants.OTP_MAX_ATTEMPTS) {
                validOtpForContact.setExpired(true);
                otpRepository.flush();
                throw new OtpException(NMRConstants.OTP_ATTEMPTS_EXCEEDED);
            }
            validOtpForContact.setAttempts(validOtpForContact.getAttempts() + 1);
            otpRepository.flush();
            throw new OtpException(NMRConstants.OTP_INVALID);
        }
        throw new OtpException(NMRConstants.OTP_NOT_FOUND);
    }

    /**
     * Sends notification on each status change
     * @param hpProfileId to fetch mobile number and emailId of user
     * @param workFlowStatusId to fetch status of application
     * @return success/failure
     */
    @Override
    public ResponseMessageTo sendNotificationOnStatusChange(BigInteger hpProfileId, BigInteger workFlowStatusId) {

        HpProfile hpProfile= hpProfileRepository.findHpProfileById(hpProfileId);
        if(null==hpProfile){
            return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
        }

        WorkFlowStatus workFlowStatus=workFlowStatusRepository.findWorkFLowStatusById(workFlowStatusId);

        if(null==workFlowStatus){
            return new ResponseMessageTo(NMRConstants.WORKFLOW_STATUS_NOT_FOUND);
        }

        String templateId= messageSource.getMessage(workFlowStatus.getName().toLowerCase(),null, Locale.ENGLISH);

        Template template=notificationDBFClient.getTemplateById(new BigInteger(templateId));

        if(null==template){
            return new ResponseMessageTo(NMRConstants.TEMPLATE_NOT_FOUND);
        }

        NotificationRequestTo notificationRequestTo=new NotificationRequestTo();
        notificationRequestTo.setOrigin(notificationOrigin);
        notificationRequestTo.setSender(notificationSender);
        notificationRequestTo.setContentType(NMRConstants.INFO_CONTENT_TYPE);

        notificationRequestTo.setType(List.of(NotificationType.EMAIL.getNotificationType(),NotificationType.SMS.getNotificationType()));
        KeyValue emailReceiver = new KeyValue(NMRConstants.EMAIL_ID,hpProfile.getEmailId());
        KeyValue mobileReceiver = new KeyValue(NMRConstants.MOBILE,hpProfile.getMobileNumber());

        notificationRequestTo.setReceiver(List.of(emailReceiver,mobileReceiver));

        KeyValue templateKeyValue = new KeyValue();
        templateKeyValue.setKey(NMRConstants.TEMPLATE_ID);
        templateKeyValue.setValue(template.getId().toString());
        KeyValue subjectKeyValue = new KeyValue();
        subjectKeyValue.setKey(NMRConstants.SUBJECT);
        subjectKeyValue.setValue(NMRConstants.INFO_EMAIL_SUBJECT);
        KeyValue contentKeyValue = new KeyValue();
        contentKeyValue.setKey(NMRConstants.CONTENT);
        contentKeyValue.setValue(template.getMessage());
        notificationRequestTo.setNotification(List.of(templateKeyValue, subjectKeyValue, contentKeyValue));
        NotificationResponseTo response=notificationFClient.sendNotification(notificationRequestTo,Timestamp.valueOf(LocalDateTime.now()),"123");
        return new ResponseMessageTo(response.status().equalsIgnoreCase(NMRConstants.SENT_RESPONSE)?NMRConstants.SUCCESS_RESPONSE:NMRConstants.FAILURE_RESPONSE);
    }

}