package in.gov.abdm.nmr.service.impl;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import in.gov.abdm.nmr.client.NotificationFClient;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.service.OtpNotificationService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class OtpNotificationServiceImpl implements OtpNotificationService {

    @Autowired
    NmrOtpRepository nmrOtpRepository;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    NotificationFClient notificationFClient;

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
    public OtpGenerateResponseTo generateOtp(OtpGenerateRequestTo otpGenerateRequestTo) throws NoSuchAlgorithmException, OtpException, JsonProcessingException {
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
            templateKeyValue.setValue(NMRConstants.EMAIL_TEMPLATE_ID);
            KeyValue subjectKeyValue = new KeyValue();
            subjectKeyValue.setKey(NMRConstants.SUBJECT);
            subjectKeyValue.setValue(NMRConstants.EMAIL_SUBJECT);
            KeyValue contentKeyValue = new KeyValue();
            contentKeyValue.setKey(NMRConstants.CONTENT);
            contentKeyValue.setValue(NMRConstants.EMAIL_BODY + otp);
            notificationRequestTo.setNotification(List.of(templateKeyValue, subjectKeyValue, contentKeyValue));

            OtpGenerateMessageTo response=notificationFClient.sendNotification(notificationRequestTo,Timestamp.valueOf(LocalDateTime.now()),"123");
            return new OtpGenerateResponseTo(new OtpGenerateMessageTo(response.status().equalsIgnoreCase(NMRConstants.SENT_RESPONSE)?NMRConstants.SUCCESS_RESPONSE:NMRConstants.FAILURE_RESPONSE));

        } else if (NotificationType.SMS.getNotificationType().equals(otpGenerateRequestTo.getType())) {

            notificationRequestTo.setType(List.of(NotificationType.SMS.getNotificationType()));
            KeyValue receiver = new KeyValue();
            receiver.setKey(NMRConstants.MOBILE);
            receiver.setValue(otpGenerateRequestTo.getContact());
            notificationRequestTo.setReceiver(List.of(receiver));

            KeyValue templateKeyValue = new KeyValue();
            templateKeyValue.setKey(NMRConstants.TEMPLATE_ID);
            templateKeyValue.setValue(NMRConstants.SMS_TEMPLATE_ID);
            KeyValue contentKeyValue = new KeyValue();
            contentKeyValue.setKey(NMRConstants.CONTENT);
            contentKeyValue.setValue(NMRConstants.SMS_BODY_PART_ONE + otp + NMRConstants.SMS_BODY_PART_TWO);
            notificationRequestTo.setNotification(List.of(templateKeyValue, contentKeyValue));

            OtpGenerateMessageTo response=notificationFClient.sendNotification(notificationRequestTo,Timestamp.valueOf(LocalDateTime.now()),"123");
            return new OtpGenerateResponseTo(new OtpGenerateMessageTo(response.status().equalsIgnoreCase(NMRConstants.SENT_RESPONSE)?NMRConstants.SUCCESS_RESPONSE:NMRConstants.FAILURE_RESPONSE));

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

    String getConvertedResponse(String response){

        if(response.equalsIgnoreCase("sent")){
            return NMRConstants.SUCCESS_RESPONSE;
        }else {
            return NMRConstants.FAILURE_RESPONSE;
        }
    }
}
