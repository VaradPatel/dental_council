package in.gov.abdm.nmr.service;

import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.gov.abdm.nmr.dto.OtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.OtpGenerateResponseTo;
import in.gov.abdm.nmr.dto.OtpValidateRequestTo;
import in.gov.abdm.nmr.dto.OtpValidateResponseTo;
import in.gov.abdm.nmr.exception.OtpException;

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
