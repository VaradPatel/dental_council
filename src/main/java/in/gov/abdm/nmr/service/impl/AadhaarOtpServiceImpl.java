package in.gov.abdm.nmr.service.impl;
import in.gov.abdm.nmr.client.AadhaarFClient;
import in.gov.abdm.nmr.dto.AadhaarOtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.AadhaarResponseTo;
import in.gov.abdm.nmr.service.AadhaarOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import in.gov.abdm.nmr.dto.AadhaarOtpValidateRequestTo;

/**
 * Implementations of methods to send and validate Aadhaar OTP
 */
@Service
@Transactional
public class AadhaarOtpServiceImpl implements AadhaarOtpService {

    @Autowired
    AadhaarFClient aadhaarFClient;

    /**
     * Sends Aadhar OTP
     * @param otpGenerateRequestTo coming from Aadhaar OTP Service
     * @return AadhaarResponseDto Object
     */
    @Override
    public AadhaarResponseTo sendOtp(AadhaarOtpGenerateRequestTo otpGenerateRequestTo) {

        return aadhaarFClient.sendOTP(otpGenerateRequestTo);
    }

    /**
     * Verifies Aadhaar OTP
     * @param otpValidateRequestTo coming from Aadhaar OTP Service
     * @return AadhaarResponseDto Object
     */
    @Override
    public AadhaarResponseTo verifyOtp(AadhaarOtpValidateRequestTo otpValidateRequestTo) {

        return aadhaarFClient.verifyOTP(otpValidateRequestTo);
    }
}

