package in.gov.abdm.nmr.service.impl;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.exception.TemplateException;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.enums.NotificationType;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.redis.hash.Otp;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IOtpDaoService;
import in.gov.abdm.nmr.service.IOtpService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.util.NMRConstants;

/**
 * Implementation of methods to generate and validate OTP
 */
@Slf4j
@Service
public class OtpServiceImpl implements IOtpService {

    private IOtpDaoService otpDaoService;

    private IUserDaoService userDaoService;

    private INotificationService notificationService;

    private RsaUtil rsaUtil;
    
    private boolean otpEnabled;

    public OtpServiceImpl(IOtpDaoService otpDaoService, IUserDaoService userDaoService, INotificationService notificationService, RsaUtil rsaUtil, //
                          @Value("${nmr.otp.enabled}") boolean otpEnabled) {
        this.otpDaoService = otpDaoService;
        this.userDaoService = userDaoService;
        this.notificationService = notificationService;
        this.rsaUtil = rsaUtil;
        this.otpEnabled = otpEnabled;
    }

    /**
     * Generates OTP by validating with repository
     *
     * @param otpGenerateRequestTo coming from controller
     * @return Success/Failure message
     * @throws OtpException with message
     */
    @Override
    public OTPResponseMessageTo generateOtp(OtpGenerateRequestTo otpGenerateRequestTo) throws OtpException, InvalidRequestException, TemplateException {
        String notificationType = otpGenerateRequestTo.getType();

        if(!otpGenerateRequestTo.isRegistration()) {

            User user = userDaoService.findByUsername(otpGenerateRequestTo.getContact());
            if (user == null) {
                if (notificationType.equals(NotificationType.SMS.getNotificationType())) {
                    throw new InvalidRequestException(NMRError.NON_REGISTERED_MOBILE_NUMBER.getCode(), NMRError.NON_REGISTERED_MOBILE_NUMBER.getMessage());
                } else if (notificationType.equals(NotificationType.EMAIL.getNotificationType())) {
                    throw new InvalidRequestException(NMRError.NON_REGISTERED_EMAIL_ID.getCode(), NMRError.NON_REGISTERED_EMAIL_ID.getMessage());
                } else if (notificationType.equals(NotificationType.NMR_ID.getNotificationType())) {
                    throw new InvalidRequestException(NMRError.NON_REGISTERED_NMR_ID.getCode(), NMRError.NON_REGISTERED_NMR_ID.getMessage());
                }
            }
        }

        String contact = otpGenerateRequestTo.getContact();
        if (NotificationType.NMR_ID.getNotificationType().equals(otpGenerateRequestTo.getType())) {
            notificationType = NotificationType.SMS.getNotificationType();
            contact = userDaoService.findByUsername(otpGenerateRequestTo.getContact()).getMobileNumber();
        }

        List<Otp> previousOtps = otpDaoService.findAllByContact(contact);
        if (previousOtps.size() >= NMRConstants.OTP_GENERATION_MAX_ATTEMPTS) {
            throw new OtpException(NMRError.OTP_ATTEMPTS_EXCEEDED.getCode(), NMRError.OTP_ATTEMPTS_EXCEEDED.getMessage(), HttpStatus.TOO_MANY_REQUESTS.toString());
        } else {
            for (Otp previousOtp : previousOtps) {
                previousOtp.setExpired(true);
                otpDaoService.save(previousOtp);
                if (previousOtp.getAttempts() >= NMRConstants.OTP_MAX_ATTEMPTS) {
                    throw new OtpException(NMRError.OTP_ATTEMPTS_EXCEEDED.getCode(), NMRError.OTP_ATTEMPTS_EXCEEDED.getMessage(), HttpStatus.TOO_MANY_REQUESTS.toString());
                }
            }
        }

        String otp = String.valueOf(new SecureRandom().nextInt(899999) + 100000);

        Otp otpEntity = new Otp(UUID.randomUUID().toString(), otp, 0, contact, false, 10);
        otpDaoService.save(otpEntity);
        return getOtpResponseMessageTo(otpGenerateRequestTo, contact, otpEntity, notificationService.sendNotificationForOTP(notificationType, otp, contact));
    }

    private OTPResponseMessageTo getOtpResponseMessageTo(OtpGenerateRequestTo otpGenerateRequestTo, String contact, Otp otpEntity, ResponseMessageTo notificationResponse) {
        if (notificationResponse.getMessage().equalsIgnoreCase(NMRConstants.SUCCESS_RESPONSE)) {

            if (otpGenerateRequestTo.getType().equalsIgnoreCase(NMRConstants.SMS)) {
                if (contact != null && !contact.isBlank()) {
                    contact = contact.replaceAll(contact.substring(0, 10 - 4), "xxxxxx");
                }
            } else if (otpGenerateRequestTo.getType().equalsIgnoreCase(NMRConstants.EMAIL) && contact != null && !contact.isBlank()) {
                String idPart = contact.substring(0, contact.lastIndexOf("@"));
                String domain = contact.substring(contact.lastIndexOf("@"), contact.length());
                contact = "x".repeat(idPart.length()) + domain;
            }
            return new OTPResponseMessageTo(otpEntity.getId(), NMRConstants.SUCCESS_RESPONSE, contact);
        } else {
            return new OTPResponseMessageTo(null, NMRConstants.FAILURE_RESPONSE, null);
        }
    }

    /**
     * Validated generated OTP with repository
     *
     * @param otpValidateRequestTo to validate OTP
     * @return Success/Failure message
     * @throws OtpException with message
     */
    @Override
    public OtpValidateResponseTo validateOtp(OtpValidateRequestTo otpValidateRequestTo, boolean callInternal) throws OtpException, GeneralSecurityException {
        if (!otpEnabled) {
            return new OtpValidateResponseTo(new OtpValidateMessageTo(NMRConstants.SUCCESS_RESPONSE, null, null));
        }
        
        String transactionId = otpValidateRequestTo.getTransactionId();
        if(StringUtils.isBlank(transactionId)) {
            throw new OtpException(NMRError.INVALID_OTP_TRANSACTION_ID.getCode(), NMRError.INVALID_OTP_TRANSACTION_ID.getMessage());
        }
        Otp otpDetails = otpDaoService.findById(transactionId);

        if (otpDetails == null || otpDetails.isExpired()) {
            throw new OtpException(NMRError.OTP_EXPIRED.getCode(), NMRError.OTP_EXPIRED.getMessage());
        }

        if (otpDetails.getAttempts() >= NMRConstants.OTP_MAX_ATTEMPTS) {
            otpDaoService.deleteById(otpDetails.getId());
            throw new OtpException(NMRError.OTP_ATTEMPTS_EXCEEDED.getCode(), NMRError.OTP_ATTEMPTS_EXCEEDED.getMessage(), HttpStatus.TOO_MANY_REQUESTS.toString());
        }

        String decryptedOtp = callInternal ? otpValidateRequestTo.getOtp() : rsaUtil.decrypt(otpValidateRequestTo.getOtp());
        if (decryptedOtp.equals(otpDetails.getOtp())) {
            otpDaoService.save(otpDetails);
            try {
                notificationService.sendNotificationForVerifiedOTP(otpValidateRequestTo.getType(), otpValidateRequestTo.getContact());
            }catch (Exception exception){
                log.debug("error occurred while sending notification:" + exception.getLocalizedMessage());
            }
            return new OtpValidateResponseTo(new OtpValidateMessageTo(NMRConstants.SUCCESS_RESPONSE, otpDetails.getId(), otpValidateRequestTo.getType()));
        } else {
            otpDetails.setAttempts(otpDetails.getAttempts() + 1);
            otpDaoService.save(otpDetails);
            throw new OtpException(NMRError.OTP_INVALID.getCode(), NMRError.OTP_INVALID.getMessage());
        }
    }

    @Override
    public boolean isOtpVerified(String id) {
        if (!otpEnabled) {
            return true;
        }
        
        Otp otp = otpDaoService.findById(id);
        if (otp == null) {
            return false;
        }
        otpDaoService.deleteById(id);
        return otp.isExpired();
    }
    
    @Override
    public boolean isOtpEnabled() {
        return otpEnabled;
    }
}
