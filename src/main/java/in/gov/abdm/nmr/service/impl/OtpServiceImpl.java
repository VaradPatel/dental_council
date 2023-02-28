package in.gov.abdm.nmr.service.impl;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.dto.OtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.OtpValidateMessageTo;
import in.gov.abdm.nmr.dto.OtpValidateRequestTo;
import in.gov.abdm.nmr.dto.OtpValidateResponseTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.redis.hash.Otp;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IOtpDaoService;
import in.gov.abdm.nmr.service.IOtpService;
import in.gov.abdm.nmr.util.NMRConstants;

/**
 * Implementation of methods to generate and validate OTP
 */
@Service
public class OtpServiceImpl implements IOtpService {

    IOtpDaoService otpDaoService;

    INotificationService notificationService;

    RsaUtil rsaUtil;

    public OtpServiceImpl(IOtpDaoService nmrOtpRepository, INotificationService notificationService, RsaUtil rsaUtil) {
        this.otpDaoService = nmrOtpRepository;
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
    public ResponseMessageTo generateOtp(OtpGenerateRequestTo otpGenerateRequestTo) throws OtpException {
        List<Otp> previousOtps = otpDaoService.findAllByContact(otpGenerateRequestTo.getContact());
        if (previousOtps.size() >= NMRConstants.OTP_GENERATION_MAX_ATTEMPTS) {
            throw new OtpException(NMRConstants.OTP_ATTEMPTS_EXCEEDED);
        } else {
            for (Otp previousOtp : previousOtps) {
                previousOtp.setExpired(true);
                otpDaoService.save(previousOtp);
                if (previousOtp.getAttempts() >= NMRConstants.OTP_MAX_ATTEMPTS) {
                    throw new OtpException(NMRConstants.OTP_ATTEMPTS_EXCEEDED);
                }
            }
        }

        String otp = String.valueOf(new SecureRandom().nextInt(899999) + 100000);
        Otp otpEntity = new Otp(UUID.randomUUID().toString(), otp, 0, otpGenerateRequestTo.getContact(), false, 10);
        otpDaoService.save(otpEntity);
        return notificationService.sendNotificationForOTP(otpGenerateRequestTo.getType(), otp, otpGenerateRequestTo.getContact(), otpEntity.getId());
    }

    /**
     * Validated generated OTP with repository
     *
     * @param otpValidateRequestTo to validate OTP
     * @return Success/Failure message
     * @throws OtpException with message
     */
    @Override
    public OtpValidateResponseTo validateOtp(OtpValidateRequestTo otpValidateRequestTo) throws OtpException, GeneralSecurityException {
        String transactionId = otpValidateRequestTo.getTransactionId();
        Otp otpDetails = otpDaoService.findById(transactionId);

        if (otpDetails == null || otpDetails.isExpired()) {
            throw new OtpException(NMRConstants.OTP_EXPIRED);
        }

        if (otpDetails.getAttempts() >= NMRConstants.OTP_MAX_ATTEMPTS) {
            otpDetails.setExpired(true);
            otpDaoService.save(otpDetails);
            throw new OtpException(NMRConstants.OTP_ATTEMPTS_EXCEEDED);
        }

        String decryptedOtp = rsaUtil.decrypt(otpValidateRequestTo.getOtp());
        if (decryptedOtp.equals(otpDetails.getOtp())) {
            otpDetails.setExpired(true);
            otpDaoService.save(otpDetails);
            notificationService.sendNotificationForVerifiedOTP(otpValidateRequestTo.getType(), otpValidateRequestTo.getContact(), transactionId);
            return new OtpValidateResponseTo(new OtpValidateMessageTo(NMRConstants.SUCCESS_RESPONSE, otpDetails.getId(), otpValidateRequestTo.getType()));
        } else {
            otpDetails.setAttempts(otpDetails.getAttempts() + 1);
            otpDaoService.save(otpDetails);
            throw new OtpException(NMRConstants.OTP_INVALID);
        }
    }
}
