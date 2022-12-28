package in.gov.abdm.nmr.api.controller.aadhaar;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.gov.abdm.nmr.api.controller.aadhaar.to.AadhaarOtpGenerateRequestTo;
import in.gov.abdm.nmr.api.controller.aadhaar.to.AadhaarOtpValidateRequestTo;
import in.gov.abdm.nmr.api.controller.aadhaar.to.AadhaarResponseTo;

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
