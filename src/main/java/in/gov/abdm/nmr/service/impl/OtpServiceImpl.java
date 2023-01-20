package in.gov.abdm.nmr.service.impl;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.Otp;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.repository.NmrOtpRepository;
import in.gov.abdm.nmr.repository.OtpRepository;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IOtpService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Implementation of methods to generate and validate OTP
 */
@Service
public class OtpServiceImpl implements IOtpService {

    @Autowired
    NmrOtpRepository nmrOtpRepository;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    INotificationService notificationService;

    /**
     * Generates OTP by validating with repository
     *
     * @param otpGenerateRequestTo coming from controller
     * @return Success/Failure message
     * @throws OtpException with message
     */
    @Override
    public ResponseMessageTo generateOtp(OtpGenerateRequestTo otpGenerateRequestTo) throws OtpException {
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

        return notificationService.sendNotificationForOTP(otpGenerateRequestTo.getType(), otp, otpGenerateRequestTo.getContact());
    }

    /**
     * Validated generated OTP with repository
     *
     * @param otpValidateRequestTo to validate OTP
     * @return Success/Failure message
     * @throws OtpException with message
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

                notificationService.sendNotificationForVerifiedOTP(otpValidateRequestTo.getType(), otpValidateRequestTo.getContact());

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
