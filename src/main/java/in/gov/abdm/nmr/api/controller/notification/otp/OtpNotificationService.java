package in.gov.abdm.nmr.api.controller.notification.otp;

import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.gov.abdm.nmr.api.controller.notification.otp.to.OtpGenerateRequestTo;
import in.gov.abdm.nmr.api.controller.notification.otp.to.OtpGenerateResponseTo;
import in.gov.abdm.nmr.api.controller.notification.otp.to.OtpValidateRequestTo;
import in.gov.abdm.nmr.api.controller.notification.otp.to.OtpValidateResponseTo;
import in.gov.abdm.nmr.api.exception.OtpException;

/**
 * Interface to declare methods
 */
public interface OtpNotificationService {

    /**
     * Generates API
     */
    OtpGenerateResponseTo generateOtp(OtpGenerateRequestTo otpGenerateRequestTo) throws NoSuchAlgorithmException, OtpException, JsonProcessingException;

    /**
     * Validates API
     */
    OtpValidateResponseTo validateOtp(OtpValidateRequestTo otpValidateRequestTo) throws OtpException;

}
