package in.gov.abdm.nmr.service.impl;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.NMRError;
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
@Service
public class OtpServiceImpl implements IOtpService {

    private IOtpDaoService otpDaoService;

    private IUserDaoService userDaoService;

    private INotificationService notificationService;

    private RsaUtil rsaUtil;

    public OtpServiceImpl(IOtpDaoService otpDaoService, IUserDaoService userDaoService, INotificationService notificationService, RsaUtil rsaUtil) {
        this.otpDaoService = otpDaoService;
        this.userDaoService = userDaoService;
        this.notificationService = notificationService;
        this.rsaUtil = rsaUtil;
    }

    /**
     * Generates OTP by validating with repository
     *
     * @param otpGenerateRequestTo coming from controller
     * @return Success/Failure message
     * @throws OtpException with message
     */
    @Override
    public OTPResponseMessageTo generateOtp(OtpGenerateRequestTo otpGenerateRequestTo) throws OtpException {
        String notificationType = otpGenerateRequestTo.getType();
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
        ResponseMessageTo notificationResponse = notificationService.sendNotificationForOTP(notificationType, otp, contact);

        if (notificationResponse.getMessage().equalsIgnoreCase(NMRConstants.SUCCESS_RESPONSE)) {

            if (otpGenerateRequestTo.getType().equalsIgnoreCase(NMRConstants.SMS)) {
                if (contact != null && !contact.isBlank()) {
                    contact = contact.replaceAll(contact.substring(0, 10 - 4), "xxxxxx");
                }
            } else if (otpGenerateRequestTo.getType().equalsIgnoreCase(NMRConstants.EMAIL)) {
                if (contact != null && !contact.isBlank()) {
                    String idPart = contact.substring(0, contact.lastIndexOf("@"));
                    String domain = contact.substring(contact.lastIndexOf("@"), contact.length());
                    contact = "x".repeat(idPart.length()) + domain;
                }
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
        String transactionId = otpValidateRequestTo.getTransactionId();
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
            notificationService.sendNotificationForVerifiedOTP(otpValidateRequestTo.getType(), otpValidateRequestTo.getContact());
            return new OtpValidateResponseTo(new OtpValidateMessageTo(NMRConstants.SUCCESS_RESPONSE, otpDetails.getId(), otpValidateRequestTo.getType()));
        } else {
            otpDetails.setAttempts(otpDetails.getAttempts() + 1);
            otpDaoService.save(otpDetails);
            throw new OtpException(NMRError.OTP_INVALID.getCode(), NMRError.OTP_INVALID.getMessage());
        }
    }

    @Override
    public boolean isOtpVerified(String id) {
        Otp otp = otpDaoService.findById(id);
        if (otp == null) {
            return false;
        }
        otpDaoService.deleteById(id);
        return otp.isExpired();
    }
}
