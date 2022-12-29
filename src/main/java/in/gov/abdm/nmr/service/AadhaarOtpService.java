package in.gov.abdm.nmr.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.gov.abdm.nmr.dto.AadhaarOtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.AadhaarOtpValidateRequestTo;
import in.gov.abdm.nmr.dto.AadhaarResponseTo;

/**
 * Interface to declare Aadhaar methods
 */
public interface AadhaarOtpService {

    /**
     * Generates OTP API
     */
    AadhaarResponseTo sendOtp(AadhaarOtpGenerateRequestTo otpGenerateRequestTo) throws JsonProcessingException;

    /**
     * Validate OTP API
     */
    AadhaarResponseTo verifyOtp(AadhaarOtpValidateRequestTo otpValidateRequestTo) throws JsonProcessingException;

}
