package in.gov.abdm.nmr.service.impl;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import in.gov.abdm.nmr.dto.OtpGenerateMessageTo;
import in.gov.abdm.nmr.dto.OtpValidateMessageTo;
import in.gov.abdm.nmr.service.OtpNotificationService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.service.NotificationService;
import in.gov.abdm.nmr.enums.NotificationType;
import in.gov.abdm.nmr.dto.OtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.OtpGenerateResponseTo;
import in.gov.abdm.nmr.dto.OtpValidateRequestTo;
import in.gov.abdm.nmr.dto.OtpValidateResponseTo;
import in.gov.abdm.nmr.dto.NotificationDataTo;
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
    NotificationService notificationService;
    @Autowired
    NmrOtpRepository nmrOtpRepository;
    @Autowired
    OtpRepository otpRepository;

    /**
     * Generates OTP by validating with repository
     * @param otpGenerateRequestTo coming from controller
     * @return Success/Failure message
     * @throws NoSuchAlgorithmException
     * @throws OtpException
     * @throws JsonProcessingException
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
        if (NotificationType.EMAIL.getNotificationType().equals(otpGenerateRequestTo.getType())) {
            NotificationDataTo notificationDataTo = new NotificationDataTo();
            notificationDataTo.setContent(NMRConstants.EMAIL_BODY + otp);
            notificationDataTo.setEmailId(otpGenerateRequestTo.getContact());
            notificationDataTo.setSubject(NMRConstants.EMAIL_SUBJECT);
            notificationDataTo.setContentType(NMRConstants.OTP_CONTENT_TYPE);
            notificationDataTo.setTemplateId(NMRConstants.EMAIL_TEMPLATE_ID);
            notificationService.sendOTPOnEmail(notificationDataTo);
            return new OtpGenerateResponseTo(new OtpGenerateMessageTo(NMRConstants.SUCCESS_RESPONSE));
        } else if (NotificationType.SMS.getNotificationType().equals(otpGenerateRequestTo.getType())) {
            NotificationDataTo notificationDataTo = new NotificationDataTo();
            notificationDataTo.setContent(NMRConstants.SMS_BODY_PART_ONE + otp + NMRConstants.SMS_BODY_PART_TWO);
            notificationDataTo.setContentType(NMRConstants.OTP_CONTENT_TYPE);
            notificationDataTo.setMobileNumber(otpGenerateRequestTo.getContact());
            notificationDataTo.setTemplateId(NMRConstants.SMS_TEMPLATE_ID);
            notificationService.sendOTPOnMobile(notificationDataTo);
            return new OtpGenerateResponseTo(new OtpGenerateMessageTo(NMRConstants.SUCCESS_RESPONSE));
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
}
